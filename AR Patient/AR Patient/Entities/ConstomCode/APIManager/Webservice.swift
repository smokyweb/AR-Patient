//
//  Webservice.swift
//  FoodPon_User
//
//  Created by Ilesh's  on 13/03/19.
//

import Foundation
import UIKit
import Alamofire
import SwiftyJSON

class WebService: NSObject {
    
    static let call : WebService = WebService()
    
    var isPrintLogs : Bool = true
    
    // CLOSUER DECLARATION
    typealias Success = (_ responseData: Any, _ success: Bool) -> Void
    typealias GoogleSuccess = (_ success: Bool) -> Void
    
    typealias Failure = () -> Void
    typealias FailureData = (_ Error:String, _ Flag:Bool) -> Void
    
}

/**
 @METHODS:- METHODS FOR CHECK INTERNET CONNECTION
 **/

extension WebService {
    func isNetworkAvailable() -> Bool {
        return NetworkReachabilityManager()!.isReachable
    }
}

/**
 @METHODS:- BASIC METHODS FOR CALLING API.
 **/

extension WebService {
    
    func GET(filePath: String, params: [String: Any]?, enableInteraction: Bool, showLoader: Bool, viewObj: UIView?, onSuccess: @escaping (Success), onFailure: @escaping (Failure)) {
        
        guard NetworkReachabilityManager()!.isReachable else {
            Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "ERROR_CALL"))
            return
        }
        
        let strPath = "\(APIConstant.url)"+filePath
        var viewSpinner: UIView?
        if (showLoader) {
            viewSpinner = IPLoader.showLoaderWithBG(viewObj: viewObj!, boolShow: showLoader, enableInteraction: enableInteraction)!
        }
        
        self.isPrintLogs ? print("URL:- \(strPath) \nPARAM:- \(String(describing: params))") : nil
        
        var headers : [String:String] = [:]
        if (Global.kretriveUserData().IsLoggedIn! == "1" ){
            headers = [
                //"AUTH-KEY": "\(APIConstant.headerKey)",
                "session-id": "\(Global.kretriveUserData().session_id ?? "ARP@T!ENT$")",
                "user-id": "\(Global.kretriveUserData().User_Id ?? "0")"
                //"\(Global.kretriveUserData().session_id!)-\(Global.kretriveUserData().User_Id!)-driver"
            ]
        }
        else{
            headers = [
                //"AUTH-KEY": "\(APIConstant.headerKey)",
                "session-id": "ARP@T!ENT$",
                "user-id": "0"
            ]
        }
        
        request(strPath, method: .get, parameters: params, encoding: URLEncoding() as ParameterEncoding,headers: headers).responseJSON { (response:DataResponse<Any>) in
            
            if (showLoader) {
                IPLoader.hideRemoveLoaderFromView(removableView: viewSpinner!, mainView: viewObj!)
            }
            if response.result.isSuccess {
                self.isPrintLogs ? print(response.result.value  as? AnyObject ?? "NO RESPONSE"): nil
                //                if let dictResponse = response.result.value  as? [AnyObject] {
                //                    onSuccess(dictResponse, true)
                //                }else
                if let dictResponse = response.result.value  as? [String:Any] {
                    if let msg = dictResponse["code"] as? String, msg == "401" || msg == "500" {
                        onFailure()
                    }else{
                        if let strCode = dictResponse["code"] as? Int , strCode == 0 {
                            onSuccess(dictResponse, true)
                            
                        }else if let str = dictResponse["code"] as? Int, str == 1001 {
                            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: dictResponse["message"] as? String ?? "Your session has expired. Please log in again.") { (index, title) in
                                Global.appDelegate.logoutUser()
                                //Global.appDelegate.navController.popToRootViewController(animated: true)
                            }
                        }else if "\(dictResponse["code"] ?? "")" == "1001" {
                            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: dictResponse["message"] as? String ?? "Your session has expired. Please log in again.") { (index, title) in
                                Global.appDelegate.logoutUser()
                                //Global.appDelegate.navController.popToRootViewController(animated: true)
                            }
                        }
                        else{
                            if dictResponse["error"] != nil{
                                if let dictError = dictResponse["error"] as? [String : Any]{
                                    /*Global.appDelegate.ref.child("IOS_DRIVER_LOG_TABLE").child("\(String(describing: Utility.appVersionForFirebase!))").child("\(Date())").setValue(["user_id":"\(Global.kretriveUserData().User_Id!)-Driver", "deviceName": UIDevice.current.modelName , "deviceOS": UIDevice.current.systemVersion , "log": "\(dictError)", "versionCodeName": "\(String(describing: Utility.appVersion!))(\(String(describing: Utility.appBuild!)))"])*/
                                    
                                }
                                Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "keyInternetMsg"))
                            }
                            else{
                                Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: dictResponse["message"] as? String ?? ""))
                                onFailure()
                            }
                        }
                    }
                }
            }else{
                if response.result.error?._code == NSURLErrorBadURL || response.result.error?._code == NSURLErrorUnsupportedURL || response.result.error?._code == NSURLErrorCannotFindHost || response.result.error?._code == NSURLErrorCannotConnectToHost || response.result.error?._code == NSURLErrorBadServerResponse {
                    Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "error_invalid_url"))
                }
                else if response.result.error?._code == NSURLErrorTimedOut {
                    Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "error_timeout"))
                }
                else if response.result.error?._code == NSURLErrorNetworkConnectionLost || response.result.error?._code == NSURLErrorNotConnectedToInternet{
                    Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "ERROR_CALL"))
                }
                else if response.result.error?._code == NSURLErrorSecureConnectionFailed{
                    Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "error_server_not_responding"))
                }
                else if response.result.error?._code == NSURLErrorFileDoesNotExist{
                    Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "error_file_not_found"))
                }
                else {
                    /*Global.appDelegate.ref.child("IOS_UNKNOWN_ERROR_Driver").child("\(Date())").setValue(["user_id":"\(Global.kretriveUserData().User_Id!)-Driver", "deviceName": UIDevice.current.modelName , "deviceOS": UIDevice.current.systemVersion , "log": "\(String(describing: response.result.error?.localizedDescription))", "versionCodeName": "\(String(describing: Utility.appVersion!))(\(String(describing: Utility.appBuild!)))"])
                     Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "error_unknown"))*/
                }
                
                print("Error:- \(String(describing: response.result.error?.localizedDescription))")
                onFailure()
            }
        }
    }
    
    func DOWNLOAD_FILE(url: String, enableInteraction: Bool, showLoader: Bool, viewObj: UIView?, onSuccess: @escaping (Success), onFailure: @escaping (Failure)) {
        guard NetworkReachabilityManager()!.isReachable else {
            Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "ERROR_CALL"))
            return
        }
        
        var viewSpinner: UIView?
        if (showLoader) {
            viewSpinner = IPLoader.showLoaderWithBG(viewObj: viewObj!, boolShow: showLoader, enableInteraction: enableInteraction)!
        }
        
        let destination = DownloadRequest.suggestedDownloadDestination()
        
        let manager = Alamofire.SessionManager.default
        manager.session.configuration.timeoutIntervalForRequest = 300

        Alamofire.download(url, to: destination).response { response in // method defaults to `.get`
            print("request => ", response.request)
            print("response => ", response.response)
            
            if (showLoader) {
                IPLoader.hideRemoveLoaderFromView(removableView: viewSpinner!, mainView: viewObj!)
            }
            
            if(response.error == nil) {
                print("temporaryURL => ", response.temporaryURL)
                print("destinationURL => ", response.destinationURL)
                onSuccess("\(response.destinationURL!)", true)
            } else {
                print("error => ", response.error?.localizedDescription)
                onFailure()
            }
        }
        
        /*manager.download(
            url,
            method: .get,
            parameters: [:],
            encoding: JSONEncoding.default,
            headers: nil,
            to: destination).downloadProgress(closure: { (progress) in
                //progress closure
            }).response(completionHandler: { (DefaultDownloadResponse) in
                //here you able to access the DefaultDownloadResponse
                //result closure
                print("Destination => \(destination)")
                onSuccess(true, true)
                if (showLoader) {
                    IPLoader.hideRemoveLoaderFromView(removableView: viewSpinner!, mainView: viewObj!)
                }
            })*/
    }
    
    func POST(filePath: String, params: [String: Any]?, enableInteraction: Bool, showLoader: Bool, viewObj: UIView?, onSuccess: @escaping (Success), onFailure: @escaping (Failure)) {
        
        guard NetworkReachabilityManager()!.isReachable else {
            Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "ERROR_CALL"))
            return
        }
        
        let strPath = "\(APIConstant.url)"+filePath+"?platform=\(Global.g_ws.Device_type!)&role=\(Global.kretriveUserData().role ?? "N/A")&user_lang=\(Global.g_ws.Language_type!)&version=\(Global.g_ws.Version_number!)";
        var viewSpinner: UIView?
        if (showLoader) {
            viewSpinner = IPLoader.showLoaderWithBG(viewObj: viewObj!, boolShow: showLoader, enableInteraction: enableInteraction)!
        }
        
        self.isPrintLogs ? print("URL:- \(strPath) \nPARAM:- \(String(describing: params))") : nil
        
        var headers : [String:String] = [:]
        if (Global.kretriveUserData().IsLoggedIn! == "1"){
            headers = [
                //"AUTH-KEY": "\(APIConstant.headerKey)",
                "session-id": "\(Global.kretriveUserData().session_id ?? "ARP@T!ENT$")",
                "user-id": "\(Global.kretriveUserData().User_Id ?? "0")"
                //"\(Global.kretriveUserData().session_id!)-\(Global.kretriveUserData().User_Id!)-driver"
            ]
        }
        else{
            headers = [
                //"AUTH-KEY": "\(APIConstant.headerKey)",
                "session-id": "ARP@T!ENT$",
                "user-id": "0"
                //"SESSION-KEY": "\(APIConstant.headerKey)"
            ]
        }
        print("headers*****\(headers)")
        let manager = Alamofire.SessionManager.default
        manager.session.configuration.timeoutIntervalForRequest = 120
        
        manager.request(strPath, method: .post, parameters: params, encoding: URLEncoding() as ParameterEncoding,headers: headers).responseJSON { (response:DataResponse<Any>) in
            
            if (showLoader) {
                IPLoader.hideRemoveLoaderFromView(removableView: viewSpinner!, mainView: viewObj!)
            }
            if response.result.isSuccess {
                self.isPrintLogs ? print(response.result.value  as? AnyObject ?? "NO RESPONSE"): nil
                //                if let dictResponse = response.result.value  as? [String:Any] {
                //                    onSuccess(dictResponse, true)
                //                }
                //else
                if let dictResponse = response.result.value  as? [String:Any] {
                    if let msg = dictResponse["code"] as? String, msg == "401" || msg == "500" {
                        onFailure()
                    }else{
                        if let strCode = dictResponse["code"] as? Int , strCode == 0 {
                            onSuccess(dictResponse, true)
                        }
                            //                        else if let str = dictResponse["code"] as? String, str == "1001" {
                            //                            onSuccess(dictResponse, true)
                            //                        }
                            
                        else if let str =  dictResponse["code"] as? Int, str == 5001 {
                            //onSuccess(dictResponse, true)
                            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: JSON(dictResponse["message"]!).stringValue) { (index, title) in
                                Global.appDelegate.logoutUser()
                            }
                            
                        }
                            
                        else if let str =  dictResponse["code"] as? Int, str == 1005 {
                            //onSuccess(dictResponse, true)
                            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: JSON(dictResponse["message"]!).stringValue) { (index, title) in
                                Global.appDelegate.logoutUser()
                            }
                            
                        }
                        else if let str = dictResponse["code"] as? Int, str == 1002 {
                            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: dictResponse["message"] as? String ?? "Your session has expired. Please log in again.") { (index, title) in
                                Global.appDelegate.logoutUser()
                                //Global.appDelegate.moveToRootVC()
                            }
                        }else if "\(dictResponse["code"] ?? "")" == "1002" {
                            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: dictResponse["message"] as? String ?? "Your session has expired. Please log in again.") { (index, title) in
                                Global.appDelegate.logoutUser()
                                //Global.appDelegate.moveToRootVC()
                            }
                        }
                        else{
                            if dictResponse["error"] != nil{
                                if let dictError = dictResponse["error"] as? [String : Any]{
                                    /*Global.appDelegate.ref.child("IOS_DRIVER_LOG_TABLE").child("\(String(describing: Utility.appVersionForFirebase!))").child("\(Date())").setValue(["user_id":"\(Global.kretriveUserData().User_Id!)-Driver", "deviceName": UIDevice.current.modelName , "deviceOS": UIDevice.current.systemVersion , "log": "\(dictError)", "versionCodeName": "\(String(describing: Utility.appVersion!))(\(String(describing: Utility.appBuild!)))"])*/
                                    
                                }
                                Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "keyInternetMsg"))
                            }
                            else{
                                Singleton.shared.showAlertWithSingleButton(strMessage: dictResponse["message"] as? String ?? "")
                                onFailure()
                            }
                            if let data =  response.data{
                                print("DEBUG ERROR:\n\(String(describing: String(data: data, encoding: String.Encoding.utf8)))")
                            }
                        }
                    }
                }else{
                    if let data =  response.data{
                        print("DEBUG ERROR:\n\(String(describing: String(data: data, encoding: String.Encoding.utf8)))")
                    }
                }
            }else{
                if response.result.error?._code == NSURLErrorBadURL || response.result.error?._code == NSURLErrorUnsupportedURL || response.result.error?._code == NSURLErrorCannotFindHost || response.result.error?._code == NSURLErrorCannotConnectToHost || response.result.error?._code == NSURLErrorBadServerResponse {
                    Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "error_invalid_url"))
                }
                else if response.result.error?._code == NSURLErrorTimedOut {
                    //
                    Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "error_timeout"))
                }
                else if response.result.error?._code == NSURLErrorNetworkConnectionLost || response.result.error?._code == NSURLErrorNotConnectedToInternet{
                    Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "ERROR_CALL"))
                }
                else if response.result.error?._code == NSURLErrorSecureConnectionFailed{
                    Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "error_server_not_responding"))
                }
                else if response.result.error?._code == NSURLErrorFileDoesNotExist{
                    Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "error_file_not_found"))
                }
                    
                else {
                    /*Global.appDelegate.ref.child("IOS_UNKNOWN_ERROR_Driver").child("\(Date())").setValue(["user_id":"\(Global.kretriveUserData().User_Id!)-Driver", "deviceName": UIDevice.current.modelName , "deviceOS": UIDevice.current.systemVersion , "log": "\(String(describing: response.result.error?.localizedDescription))", "versionCodeName": "\(String(describing: Utility.appVersion!))(\(String(describing: Utility.appBuild!)))"])*/
                    Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "error_unknown"))
                }
                
                print("Error:- \(String(describing: response.result.error?.localizedDescription))")
                onFailure()
            }
        }
    }
    
    
    func ImageUpload(filePath: String, params: [String: Any]?, enableInteraction: Bool, showLoader: Bool, viewObj: UIView?, onSuccess: @escaping (Success), onFailure: @escaping (Failure)) {
        
        guard NetworkReachabilityManager()!.isReachable else {
            Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "ERROR_CALL"))
            return
        }
        
        let strPath = "\(APIConstant.url)"+filePath+"?platform=\(Global.g_ws.Device_type!)&role=\(Global.kretriveUserData().role ?? "N/A")&user_lang=\(Global.g_ws.Language_type!)&version=\(Global.g_ws.Version_number!)";
        self.isPrintLogs ? print("URL:- \(strPath) \nPARAM:- \(String(describing: params))"):nil
        var viewSpinner: UIView?
        if (showLoader) {
            viewSpinner = IPLoader.showLoaderWithBG(viewObj: viewObj!, boolShow: showLoader, enableInteraction: enableInteraction)!
        }
        
        var headers : [String:String] = [:]
        if (Global.kretriveUserData().IsLoggedIn! == "1"){
            headers = [
                //"AUTH-KEY": "\(APIConstant.headerKey)",
                "session-id": "\(Global.kretriveUserData().session_id ?? "ARP@T!ENT$")",
                "user-id": "\(Global.kretriveUserData().User_Id ?? "0")"
                //"\(Global.kretriveUserData().session_id!)-\(Global.kretriveUserData().User_Id!)-driver"
            ]
        }
        else{
            headers = [
                //"AUTH-KEY": "\(APIConstant.headerKey)",
                "session-id": "ARP@T!ENT$",
                "user-id": "0"
            ]
        }
        
        Alamofire.upload(multipartFormData:{ multipartFormData in
            for (key, value) in params! {
                if let url = value as? URL{
                    multipartFormData.append(url, withName: key)
                }else if let imageData = value as? Data{
                    multipartFormData.append(imageData, withName: key, fileName: "photo.jpg", mimeType: "image/jpg")
                }
                else if let numberValue = value as? NSNumber {
                    multipartFormData.append(numberValue.description.data(using: .utf8)!, withName: key)
                }
                    //else if let stringValue = value as? String
                    //                else if let dicValue = value as? NSArray {
                    //                    for (dicKey, dicValue) in dicValue {
                    //                        self.isPrintLogs ? print("key = \(dicKey) && value = \(dicValue)"):nil
                    //                        if let imageData = dicValue as? Data{
                    //                            multipartFormData.append(imageData, withName: dicKey as! String, fileName: "doc.jpg", mimeType: "image/jpg")
                    //                        }
                    //                    }
                    //                }
                else if let dicValue = value as? NSDictionary {
                    for (dicKey, dicValue) in dicValue {
                        self.isPrintLogs ? print("key = \(dicKey) && value = \(dicValue)"):nil
                        if let imageData = dicValue as? Data{
                            multipartFormData.append(imageData, withName: dicKey as! String, fileName: "doc.jpg", mimeType: "image/jpg")
                        }
                    }
                }
                else
                {
                    self.isPrintLogs ? print("key = \(key) && value = \((value as AnyObject).data(using: String.Encoding.utf8.rawValue)!)"):nil
                    multipartFormData.append((value as AnyObject).data(using: String.Encoding.utf8.rawValue)!, withName: key)
                }
            }
        },usingThreshold:UInt64.init(),to:strPath,
          method:.post,
          headers:headers,
          encodingCompletion: { result in
            switch result {
            case .success(let upload, _, _):
                upload.uploadProgress(closure: { (progress) in
                    self.isPrintLogs ? print("Upload Progress: \(progress.fractionCompleted)"):nil
                })
                
                upload.responseJSON { response in
                    self.isPrintLogs ? debugPrint(response):nil
                    if (showLoader) {
                        IPLoader.hideRemoveLoaderFromView(removableView: viewSpinner!, mainView: viewObj!)
                    }
                    //                    if let dictResponse = response.result.value  as? [AnyObject] {
                    //                        onSuccess(dictResponse, true)
                    //                    }
                    //                    else
                    if let dictResponse = response.result.value  as? [String:AnyObject] {
                        if let msg = dictResponse["code"] as? String, msg == "401" || msg == "500" {
                            onFailure()
                        }else{
                            self.isPrintLogs ? print("Response Image \(dictResponse)"):nil
                            if let strCode = dictResponse["code"] as? Int , strCode == 0 {
                                onSuccess(dictResponse, true)
                            }else if let strCode = dictResponse["code"] as? Int , strCode == 5001 {
                                onSuccess(dictResponse, true)
                            }else if let str = dictResponse["code"] as? Int, str == 1001 {
                                AJAlertController.initialization().showAlertWithOkButton(aStrMessage: dictResponse["message"] as? String ?? "Your session has expired. Please log in again.") { (index, title) in
                                    Global.appDelegate.logoutUser()
                                    //Global.appDelegate.navController.popToRootViewController(animated: true)
                                }
                            }else if "\(dictResponse["code"] ?? "" as AnyObject)" == "1001" {
                                AJAlertController.initialization().showAlertWithOkButton(aStrMessage: dictResponse["message"] as? String ?? "Your session has expired. Please log in again.") { (index, title) in
                                    Global.appDelegate.logoutUser()
                                    //Global.appDelegate.navController.popToRootViewController(animated: true)
                                }
                            }else{
                                if dictResponse["error"] != nil{
                                    if let dictError = dictResponse["error"] as? [String : Any]{
                                        /* Global.appDelegate.ref.child("IOS_DRIVER_LOG_TABLE").child("\(String(describing: Utility.appVersionForFirebase!))").child("\(Date())").setValue(["user_id":"\(Global.kretriveUserData().User_Id!)-Driver", "deviceName": UIDevice.current.modelName , "deviceOS": UIDevice.current.systemVersion , "log": "\(dictError)", "versionCodeName": "\(String(describing: Utility.appVersion!))(\(String(describing: Utility.appBuild!)))"])*/
                                        
                                    }
                                    Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "keyInternetMsg"))
                                }
                                else{
                                    Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: dictResponse["message"] as? String ?? ""))
                                    onFailure()
                                }
                            }
                        }
                    }
                }
            case .failure(let encodingError):
                if encodingError._code == NSURLErrorBadURL || encodingError._code == NSURLErrorUnsupportedURL || encodingError._code == NSURLErrorCannotFindHost || encodingError._code == NSURLErrorCannotConnectToHost || encodingError._code == NSURLErrorBadServerResponse {
                    Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "error_invalid_url"))
                }
                else if encodingError._code == NSURLErrorTimedOut {
                    Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "error_timeout"))
                }
                else if encodingError._code == NSURLErrorNetworkConnectionLost || encodingError._code == NSURLErrorNotConnectedToInternet{
                    Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "ERROR_CALL"))
                }
                else if encodingError._code == NSURLErrorSecureConnectionFailed{
                    Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "error_server_not_responding"))
                }
                else if encodingError._code == NSURLErrorFileDoesNotExist{
                    Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "error_file_not_found"))
                }
                else {
                    /*Global.appDelegate.ref.child("IOS_UNKNOWN_ERROR_Driver").child("\(Date())").setValue(["user_id":"\(Global.kretriveUserData().User_Id!)-Driver", "deviceName": UIDevice.current.modelName , "deviceOS": UIDevice.current.systemVersion , "log": "\(String(describing: encodingError.localizedDescription))", "versionCodeName": "\(String(describing: Utility.appVersion!))(\(String(describing: Utility.appBuild!)))"])
                     Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "error_unknown"))*/
                }
                
                if (showLoader) {
                    IPLoader.hideRemoveLoaderFromView(removableView: viewSpinner!, mainView: viewObj!)
                }
                print("Error:- \(encodingError)")
                onFailure()
            }
        })
    }
}

public extension UIDevice {
    
    /// pares the deveice name as the standard name
    var modelName: String {
        
        #if targetEnvironment(simulator)
        let identifier = ProcessInfo().environment["SIMULATOR_MODEL_IDENTIFIER"]!
        #else
        var systemInfo = utsname()
        uname(&systemInfo)
        let machineMirror = Mirror(reflecting: systemInfo.machine)
        let identifier = machineMirror.children.reduce("") { identifier, element in
            guard let value = element.value as? Int8 , value != 0 else { return identifier }
            return identifier + String(UnicodeScalar(UInt8(value)))
        }
        #endif
        
        switch identifier {
        case "iPod5,1":                                 return "iPod Touch 5"
        case "iPod7,1":                                 return "iPod Touch 6"
        case "iPhone3,1", "iPhone3,2", "iPhone3,3":     return "iPhone 4"
        case "iPhone4,1":                               return "iPhone 4s"
        case "iPhone5,1", "iPhone5,2":                  return "iPhone 5"
        case "iPhone5,3", "iPhone5,4":                  return "iPhone 5c"
        case "iPhone6,1", "iPhone6,2":                  return "iPhone 5s"
        case "iPhone7,2":                               return "iPhone 6"
        case "iPhone7,1":                               return "iPhone 6 Plus"
        case "iPhone8,1":                               return "iPhone 6s"
        case "iPhone8,2":                               return "iPhone 6s Plus"
        case "iPhone9,1", "iPhone9,3":                  return "iPhone 7"
        case "iPhone9,2", "iPhone9,4":                  return "iPhone 7 Plus"
        case "iPhone8,4":                               return "iPhone SE"
        case "iPhone10,1", "iPhone10,4":                return "iPhone 8"
        case "iPhone10,2", "iPhone10,5":                return "iPhone 8 Plus"
        case "iPhone10,3", "iPhone10,6":                return "iPhone X"
        case "iPhone11,2":                              return "iPhone XS"
        case "iPhone11,4", "iPhone11,6":                return "iPhone XS Max"
        case "iPhone11,8":                              return "iPhone XR"
        case "iPad2,1", "iPad2,2", "iPad2,3", "iPad2,4":return "iPad 2"
        case "iPad3,1", "iPad3,2", "iPad3,3":           return "iPad 3"
        case "iPad3,4", "iPad3,5", "iPad3,6":           return "iPad 4"
        case "iPad4,1", "iPad4,2", "iPad4,3":           return "iPad Air"
        case "iPad5,3", "iPad5,4":                      return "iPad Air 2"
        case "iPad6,11", "iPad6,12":                    return "iPad 5"
        case "iPad7,5", "iPad7,6":                      return "iPad 6"
        case "iPad2,5", "iPad2,6", "iPad2,7":           return "iPad Mini"
        case "iPad4,4", "iPad4,5", "iPad4,6":           return "iPad Mini 2"
        case "iPad4,7", "iPad4,8", "iPad4,9":           return "iPad Mini 3"
        case "iPad5,1", "iPad5,2":                      return "iPad Mini 4"
        case "iPad6,3", "iPad6,4":                      return "iPad Pro 9.7 Inch"
        case "iPad6,7", "iPad6,8":                      return "iPad Pro 12.9 Inch"
        case "iPad7,1", "iPad7,2":                      return "iPad Pro (12.9-inch) (2nd generation)"
        case "iPad7,3", "iPad7,4":                      return "iPad Pro (10.5-inch)"
        case "iPad8,1", "iPad8,2", "iPad8,3", "iPad8,4":return "iPad Pro (11-inch)"
        case "iPad8,5", "iPad8,6", "iPad8,7", "iPad8,8":return "iPad Pro (12.9-inch) (3rd generation)"
        case "AppleTV5,3":                              return "Apple TV"
        case "AppleTV6,2":                              return "Apple TV 4K"
        case "AudioAccessory1,1":                       return "HomePod"
        default:                                        return identifier
        }
    }
    
}
