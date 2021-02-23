
//  IPLocation.swift
//
//

import Foundation
import CoreLocation
import UIKit

enum LocationType {
    case current
    case continues
}

class IPLocation : NSObject {
    
    static let manager : IPLocation = IPLocation()
    // PRINT RUN TIME PRINTS LOGS
    var isPrintLogs : Bool = true
    
    
    typealias LocationBlock = ((Error?, CLLocation?) -> Void)?
    private var block : LocationBlock?
    //SENDLOCATON WHEN DISABLE
    private var isCurrentLocation: Bool = false
    private var isUpdatingLocation: Bool = false
    
    var locationType : LocationType = .current
    
    lazy var locationManager: CLLocationManager = {
        let location = CLLocationManager()
        location.delegate = self
        location.desiredAccuracy = kCLLocationAccuracyBest
        location.requestWhenInUseAuthorization()
        //location.requestAlwaysAuthorization()
        return location
    }()
    
    func CheckLocationPermission(isForce:Bool = false) -> CLAuthorizationStatus {
        let authStatus = CLLocationManager.authorizationStatus()
        if authStatus == .notDetermined {
            Singleton.shared.isLocationEnabled = false
            locationManager.requestWhenInUseAuthorization()
        }
        if authStatus == .denied || authStatus == .restricted {
            Singleton.shared.isLocationEnabled = false
            Global().delay(delay: 0.5, closure: {
                if isForce {
                    AJAlertController.initialization().showAlertForLocation(aStrMessage: "KeyLocationEnableMessage".localize, completion: { (index, strTitle) in
                        if index == 0 {
                            if #available(iOS 10.0, *) {
                                UIApplication.shared.open(URL(string:UIApplication.openSettingsURLString)!)
                            } else {
                                UIApplication.shared.openURL(NSURL(string: UIApplication.openSettingsURLString)! as URL)
                            }
                        }
                    })
                }else{
                    AJAlertController.initialization().showAlert(aStrMessage: "KeyLocationEnableMessage".localize, aCancelBtnTitle:"No", aOtherBtnTitle: "Go to Settings", completion: { (index, strTitle) in
                        if index == 1 {
                            if #available(iOS 10.0, *) {
                                UIApplication.shared.open(URL(string:UIApplication.openSettingsURLString)!)
                            } else {
                                UIApplication.shared.openURL(NSURL(string: UIApplication.openSettingsURLString)! as URL)
                            }
                        }
                    })
                }
            })
        }
        else{
            Singleton.shared.isLocationEnabled = true
        }
        return authStatus
    }
    
    //MARK:- GET CURRENT LOCAITON
    func getCurrentLocation(location:LocationBlock){
        let auth = self.CheckLocationPermission()
        self.block = location
        self.locationType = .current
        if auth == .authorizedWhenInUse  || auth == .authorizedAlways  {
            self.locationManager.startUpdatingLocation()
        }else if auth == .notDetermined{
            self.isCurrentLocation = true
        }
    }
    
    func getContinuesLocation(location:LocationBlock){
        let auth = self.CheckLocationPermission()
        self.block = location
        self.locationType = .continues
        if auth == .authorizedWhenInUse  || auth == .authorizedAlways  {
            self.locationManager.startMonitoringSignificantLocationChanges()
        }else if auth == .notDetermined{
            self.isUpdatingLocation = true
        }
    }
    
    func stopContinuesUpdate(){
        self.locationManager.stopUpdatingLocation()
        self.locationManager.stopMonitoringSignificantLocationChanges()
        self.block = nil
    }
    
    func getAddressFromLatLon(location: CLLocationCoordinate2D) -> String {
        let ceo: CLGeocoder = CLGeocoder()
        var addressString : String = ""
        
        let loc: CLLocation = CLLocation(latitude:location.latitude, longitude: location.longitude)
        
        ceo.reverseGeocodeLocation(loc, completionHandler:
            {(placemarks, error) in
                if (error != nil)
                {
                    print("reverse geodcode fail: \(error!.localizedDescription)")
                }
                let pm = placemarks! as [CLPlacemark]
                
                if pm.count > 0 {
                    let pm = placemarks![0]
                    
                    
                    if pm.subLocality != nil {
                        addressString = addressString + pm.subLocality! + ", "
                    }
                    if pm.thoroughfare != nil {
                        addressString = addressString + pm.thoroughfare! + ", "
                    }
                    if pm.locality != nil {
                        addressString = addressString + pm.locality! + ", "
                    }
                    if pm.country != nil {
                        addressString = addressString + pm.country! + ", "
                    }
                    if pm.postalCode != nil {
                        addressString = addressString + pm.postalCode! + " "
                    }
                    
                    print("addressString \(addressString)")
                    
                }
        })
        return addressString
    }
}
//MARK:- DELEGET METHODS
extension IPLocation : CLLocationManagerDelegate {
    
    //MARK:- CHANGE AUTHORIZATION
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        if status == .authorizedAlways {
            if CLLocationManager.isMonitoringAvailable(for: CLBeaconRegion.self) {
                if CLLocationManager.isRangingAvailable() {
                    // do stuff
                }
            }
        }
        
        switch status {
        case .restricted, .denied:
            // Disable your app's location features
            //disableMyLocationBasedFeatures()
            break
            
        case .authorizedWhenInUse:
            // Enable only your app's when-in-use features.
            //enableMyWhenInUseFeatures()
            if isCurrentLocation {
                self.locationManager.startUpdatingLocation()
                self.isCurrentLocation = false
            }else if isUpdatingLocation {
                self.locationManager.startUpdatingLocation()
                self.isUpdatingLocation = false
            }
            break
            
        case .authorizedAlways:
            // Enable any of your app's location services.
            //enableMyAlwaysFeatures()
            if isCurrentLocation {
                self.locationManager.startUpdatingLocation()
                self.isCurrentLocation = false
            }else if isUpdatingLocation {
                self.locationManager.startUpdatingLocation()
                self.isUpdatingLocation = false
            }
            break
            
        case .notDetermined:
            break
        }
        
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        if self.locationType == .current {
            self.locationManager.stopUpdatingLocation()
            self.block??(nil, locations.last)
            self.isPrintLogs == false ? nil:print("didUpdateLocations ||current :-\(String(describing: locations.last))")
        }else if self.locationType == .continues {
            self.block??(nil, locations.last)
            self.isPrintLogs == false ? nil:print("didUpdateLocations ||continues :-\(String(describing: locations.last))")
        }
        
    }
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        if self.locationType == .current {
            self.locationManager.stopUpdatingLocation()
            self.block??(error, nil)
            self.isPrintLogs == false ? nil:print("didFailWithError ||current :-\(error.localizedDescription)")
        }else if self.locationType == .continues {
            self.block??(error, nil)
            self.isPrintLogs == false ? nil:print("didFailWithError ||continues :-\(error.localizedDescription)")
        }
        
    }
}
