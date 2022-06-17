//
//  APIConstant.swift
//  StructureApp
//
//  Copyright © 2017 Jigar Patel. All rights reserved.
//

import Foundation

fileprivate enum BuildForServer : String {
    case production
    case developer
    static let `default` = BuildForServer.developer
}

/// This is the Structure for API
struct APIConstant {
    static let headerKey = ""//ezpickup2019
    
    static var url : String {
        if BuildForServer.default == .developer {
            return  developement.APIURL
        }else {
            return live.APIURL
        }
    }
    
    private struct developement {
        static let baseUrl  = "https://arpatientsapp.com/"//http://arpatient.betaplanets.com"
        private static let appVersion   = "v17"
        static let APIURL  = "\(baseUrl)/wp-json/\(appVersion)"
    }
    
    private struct live {
        static let baseUrl  = "https://foodpon.betaplanets.com"
        private static let appVersion   = "v1"
        static let APIURL  = "\(baseUrl)/wp-json/\(appVersion)"
    }
    
    static var publicImgUrl : String {
        if BuildForServer.default == .developer {
            return  developement.baseUrl
        }else {
            return live.baseUrl
        }
    }

    static let mediaURL = "https://arpatientsapp.com/"//"http://arpatient.betaplanets.com"
    
    
    /// Structure for URL. This will have the API end point for the server.
    // MARK: - Basic Response keys
    
    /// Structure for API Response Keys. This will use to get the data or anything based on the key from the repsonse. Do not directly use the key rather define here and use it.
    struct Response {
        
        /// API.Response.success
        static let success                                  = "success"
        
        /// API.Response.message
        static let message                                  = "message"
        
        /// API.Response.statusCode
        static let statusCode                               = "statusCode"
        
        /// API.Response.data
        static let data                                     = "data"
    }
    
    // MARK: - Success Failure keys
    
    /// Structure for API Response Success or Failure. This will use to check that if API has responded success or failure
    struct Check {
        /// API.Check.success
        static let success                                   = "1"
        
        /// API.Check.failure
        static let failure                                   = "0"
    }
    
    struct Request {
        static let signUp            = "/users/signup"
        static let login             = "/users/login"
        static let forgotPassword    = "/users/forgot_password"
        static let resetPassword     = "/users/reset_password"
        static let changePassword    = "/users/change_password"
        static let logout            = "/users/logout"
        static let delete            = "/users/delete"
        static let faqs_list         = "/faqs/list"
        static let newsfeeds_list    = "/newsfeeds/list"
        static let faqs_detail       = "/faqs/detail"
        static let newsfeeds_detail  = "/newsfeeds/detail"
        static let CMS               = "/init/cms"
        static let faqList           = "/faqs/list"
        static let newsList          = "/newsfeeds/list"
        static let patientList       = "/patients/types"
        static let contactUs         = "/users/contact_us"
        static let changeProfile     = "/users/change_profile"
        static let changeAvatar      = "/users/change_avatar"
        static let voiceExam         = "/tests/ve_definition"
        static let voiceExamSubmit   = "/tests/ve_submit"
        static let testDiagnosis     = "/tests/diagnosis"
        static let leaderboard       = "/tests/leaderboard"
        static let testResult        = "/tests/result"
        static let myProfile         = "/users/profile"
        static let practExam         = "/tests/pe_definition"
        static let practExamSubmit   = "/tests/pe_submit"
        static let inAppPurchse      = "/inapp_purchase/create"
        
    }
}
