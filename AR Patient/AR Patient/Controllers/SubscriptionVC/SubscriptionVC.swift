//
//  SubscriptonVC.swift
//  AR Patient
//
//  Created by Knoxweb on 01/09/21.
//  Copyright © 2021 Knoxweb. All rights reserved.
//

import UIKit
import StoreKit
import SwiftyJSON
import SwiftyStoreKit

class SubscriptionVC: UIViewController {
    
    @IBOutlet weak var btnBack: UIButton!
    @IBOutlet weak var lblTitle: IPAutoScalingLabel!
    @IBOutlet weak var tableview: UITableView!
    @IBOutlet weak var tableviewHeight: NSLayoutConstraint!
    @IBOutlet weak var lblDescription: IPAutoScalingLabel!
    @IBOutlet weak var lblActiveSub: IPAutoScalingLabel!
    @IBOutlet weak var btnTerms: IPButton!
    @IBOutlet weak var btnPrivacy: IPButton!
    
    //monthly
    @IBOutlet weak var lblMonthlyPlanTitle: IPAutoScalingLabel!
    @IBOutlet weak var lblMonthlyPriceLeft: IPAutoScalingLabel!
    @IBOutlet weak var lblMonthlyPriceRight: IPAutoScalingLabel!
    @IBOutlet weak var btnMonthlyArrow: UIButton!
    @IBOutlet weak var btnYellowMonthly: IPButton!
    @IBOutlet weak var btnYellowMonthlyHeight: NSLayoutConstraint! //30
    @IBOutlet weak var monthlyMainView: UIView!
    @IBOutlet weak var btnMonthly: UIButton!
    
    //yearly
    @IBOutlet weak var lblYearlyPlanTitle: IPAutoScalingLabel!
    @IBOutlet weak var lblYearlyPriceLeft: IPAutoScalingLabel!
    @IBOutlet weak var lblYearlyPriceRight: IPAutoScalingLabel!
    @IBOutlet weak var btnYearlyArrow: UIButton!
    @IBOutlet weak var btnYellowYearly: IPButton!
    @IBOutlet weak var btnYellowYearlyHeight: NSLayoutConstraint! //30
    @IBOutlet weak var yearlyMainView: UIView!
    @IBOutlet weak var btnYearly: UIButton!
    
    @IBOutlet weak var btnManageSub: IPButton!
    @IBOutlet weak var btnManageSubHeight: NSLayoutConstraint! //55
    
    
    var productsArray = [SKProduct]()
    var tag = 1 //0 = from menu and 1 = from MyListVC

    override func viewDidLoad() {
        super.viewDidLoad()
        self.setup()
//        self.tableview.register(UINib(nibName: "SubscriptionContentCell", bundle: nil), forCellReuseIdentifier: "SubscriptionContentCell")
//        self.tableview.delegate = self
//        self.tableview.dataSource = self
//        self.tableview.separatorStyle = .none
//        self.tableviewHeight.constant = CGFloat(5 * 85)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        
    }
    
    override func viewDidDisappear(_ animated: Bool) {
//        NotificationCenter.default.removeObserver(self, name: NSNotification.Name(rawValue: "updateData"), object: nil)
    }
    
    //MARK:- CUSTOM METHODS
    func setup(){
        self.btnBack.setTitle("", for: .normal)
        self.btnBack.setImage(UIImage(named: "Back"), for: .normal)
    
        if(Singleton.shared.is_subscribed == "0"){
            self.lblActiveSub.isHidden = true
            self.lblActiveSub.text = ""
            self.btnManageSub.isHidden = true
            self.btnManageSubHeight.constant = 0
            self.lblYearlyPriceRight.isHidden = true
            self.lblMonthlyPriceRight.isHidden = true
            self.lblYearlyPriceLeft.isHidden = false
            self.lblMonthlyPriceLeft.isHidden = false
            self.btnMonthlyArrow.isHidden = false
            self.btnYearlyArrow.isHidden = false
            self.btnYellowMonthly.isHidden = false
            self.btnYellowMonthlyHeight.constant = 30
            self.btnYellowYearly.isHidden = false
            self.btnYellowYearlyHeight.constant = 30
            self.monthlyMainView.backgroundColor = UIColor(named: "Color_E5E5E5")
            self.yearlyMainView.backgroundColor = UIColor(named: "Color_E5E5E5")
            self.monthlyMainView.alpha = 1
            self.yearlyMainView.alpha = 1
            self.btnMonthly.isEnabled = true
            self.btnYearly.isEnabled = true
        } else{
            let date = Singleton.shared.subscription_exp_date.convertTODate()
            if(Singleton.shared.subscription_bundle_id == "com.ar.patient.silver"){
                self.lblMonthlyPriceLeft.text = "Renewal Date: \(date.dateTimeString(withFormate: "MMM dd, yyyy"))"
                self.monthlyMainView.backgroundColor = UIColor(named: "Color_fbc971")
                self.btnMonthly.isEnabled = false
                self.btnYellowMonthly.isHidden = true
                self.btnYellowMonthlyHeight.constant = 0
                self.btnMonthlyArrow.isHidden = true
            } else{
                self.lblYearlyPriceLeft.text = "Renewal Date: \(date.dateTimeString(withFormate: "MMM dd, yyyy"))"
                self.yearlyMainView.backgroundColor = UIColor(named: "Color_fbc971")
                self.btnYearly.isEnabled = false
                self.btnYellowYearly.isHidden = true
                self.btnYellowYearlyHeight.constant = 0
                self.btnYearlyArrow.isHidden = true
            }
            self.lblActiveSub.isHidden = false
            self.lblActiveSub.text = "Active Subscriptions"
            self.btnManageSub.isHidden = false
            self.btnManageSubHeight.constant = 55
            self.lblYearlyPriceLeft.isHidden = false
            self.lblMonthlyPriceLeft.isHidden = false
            self.lblYearlyPriceRight.isHidden = false
            self.lblMonthlyPriceRight.isHidden = false
        }
        
        NotificationCenter.default.addObserver(self, selector: #selector(self.updateData(_:)), name: NSNotification.Name(rawValue: "updateData"), object: nil)
        
//        self.tableview.register(UINib(nibName: "SubscriptionContentCell", bundle: nil), forCellReuseIdentifier: "SubscriptionContentCell")
//        self.tableview.delegate = self
//        self.tableview.dataSource = self
//        self.tableview.separatorStyle = .none
//        self.tableviewHeight.constant = CGFloat(Singleton.shared.arrSubscriptionData.count * 85)
//        self.tableview.reloadData()
        
        self.lblDescription.text = Singleton.shared.subscription_content
        //self.lblTitle.text = Singleton.shared.subscription_title
        
        self.retrievePackages()
    }
    
    func retrievePackages() {
        PKIAPHandler.shared.setProductIds(ids: self.retrieveProductIDs())
        PKIAPHandler.shared.fetchAvailableProducts { [weak self](products)   in
            guard let sSelf = self else {return}
            sSelf.productsArray = products
            
            print("products ======== \(products)")
            
            //sSelf.tableView.reloadData() //reload you table or collection view
            DispatchQueue.main.async {
                for i in products{
                    if(i.productIdentifier == "com.ar.patient.silver"){
                        if(Singleton.shared.is_subscribed == "1"){
                            if(i.productIdentifier != Singleton.shared.subscription_bundle_id){
                                self?.lblMonthlyPriceLeft.text = "\(i.priceLocale.currencySymbol!)\(i.price)"
                            }
                        } else{
                            self?.lblMonthlyPriceLeft.text = "\(i.priceLocale.currencySymbol!)\(i.price)"
                        }
                        self?.lblMonthlyPriceRight.text = "\(i.priceLocale.currencySymbol!)\(i.price)"
                    } else if(i.productIdentifier == "com.ar.patient.gold"){
                        if(Singleton.shared.is_subscribed == "1"){
                            if(i.productIdentifier != Singleton.shared.subscription_bundle_id){
                                self?.lblYearlyPriceLeft.text = "\(i.priceLocale.currencySymbol!)\(i.price)"
                            }
                        } else{
                            self?.lblYearlyPriceLeft.text = "\(i.priceLocale.currencySymbol!)\(i.price)"
                        }
                        self?.lblYearlyPriceRight.text = "\(i.priceLocale.currencySymbol!)\(i.price)"
                    }
                }
                
            }
            
        }
    }
    
    func retrieveProductIDs() -> [String]{
        var productID: [String] = []
        if let URL = Bundle.main.url(forResource: "IAP_ProductIDs", withExtension: "plist") {
            if let englishFromPlist = NSArray(contentsOf: URL) as? [String] {
                productID = englishFromPlist
            }
        }
        return productID
    }
    
    func purchaseSubscription(tag: Int) {
        if(self.productsArray.count > 1) {
            if(tag == 1){ //Silver
                var position = 0
                if(self.productsArray[0].productIdentifier == "com.ar.patient.gold") {
                    position = 1
                }
                SwiftyStoreKit.purchaseProduct(self.productsArray[position].productIdentifier, quantity: 1, atomically: true) { result in
                    switch result {
                    case .success(let product):
                        // fetch content from your server, then:
                        if product.needsFinishTransaction {
                            SwiftyStoreKit.finishTransaction(product.transaction)
                        }
                        print("Purchase Success: \(product.productId)")
                        let txnID = product.transaction.transactionIdentifier!
                        let originalTxnID = product.originalTransaction?.transactionIdentifier ?? ""
                        let currency = product.product.priceLocale.currencyCode
                        let productID = product.product.productIdentifier
                        let txnDate = product.transaction.transactionDate
                        let price = "\(product.product.price)"
                        
                        self.inAppPurchaseAPI(productID: productID, txnDate: txnDate!, currency: currency!, txnID: txnID, originalTxnID: originalTxnID, price: price)
                    case .error(let error):
                        switch error.code {
                        case .unknown: print("Unknown error. Please contact support")
                        case .clientInvalid: print("Not allowed to make the payment")
                        case .paymentCancelled: break
                        case .paymentInvalid: print("The purchase identifier was invalid")
                        case .paymentNotAllowed: print("The device is not allowed to make the payment")
                        case .storeProductNotAvailable: print("The product is not available in the current storefront")
                        case .cloudServicePermissionDenied: print("Access to cloud service information is not allowed")
                        case .cloudServiceNetworkConnectionFailed: print("Could not connect to the network")
                        case .cloudServiceRevoked: print("User has revoked permission to use this cloud service")
                        default: print((error as NSError).localizedDescription)
                        }
                    }
                }
            } else if(tag == 2){ //Gold
                var position = 1
                if(self.productsArray[1].productIdentifier == "com.ar.patient.silver") {
                    position = 0
                }
                SwiftyStoreKit.purchaseProduct(self.productsArray[position].productIdentifier, quantity: 1, atomically: true) { result in
                    switch result {
                    case .success(let product):
                        // fetch content from your server, then:
                        if product.needsFinishTransaction {
                            SwiftyStoreKit.finishTransaction(product.transaction)
                        }
                        print("Purchase Success: \(product.productId)")
                        let txnID = product.transaction.transactionIdentifier!
                        let originalTxnID = product.originalTransaction?.transactionIdentifier ?? ""
                        let currency = product.product.priceLocale.currencyCode
                        let productID = product.product.productIdentifier
                        let txnDate = product.transaction.transactionDate
                        let price = "\(product.product.price)"
                        
                        self.inAppPurchaseAPI(productID: productID, txnDate: txnDate!, currency: currency!, txnID: txnID, originalTxnID: originalTxnID, price: price)
                    case .error(let error):
                        switch error.code {
                        case .unknown: print("Unknown error. Please contact support")
                        case .clientInvalid: print("Not allowed to make the payment")
                        case .paymentCancelled: break
                        case .paymentInvalid: print("The purchase identifier was invalid")
                        case .paymentNotAllowed: print("The device is not allowed to make the payment")
                        case .storeProductNotAvailable: print("The product is not available in the current storefront")
                        case .cloudServicePermissionDenied: print("Access to cloud service information is not allowed")
                        case .cloudServiceNetworkConnectionFailed: print("Could not connect to the network")
                        case .cloudServiceRevoked: print("User has revoked permission to use this cloud service")
                        default: print((error as NSError).localizedDescription)
                        }
                    }
                }
            }
        }
    }
    
    //MARK:- ACTION METHODS
    @IBAction func btnBack(_ sender: Any) {
        self.view.endEditing(true)
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func btnTerms(_ sender: Any) {
        self.view.endEditing(true)
        let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "WebViewController") as? WebViewController
        vc?.cms_id = Singleton.shared.cms_id_terms
        self.navigationController?.pushViewController(vc!, animated: true)
    }
    
    @IBAction func btnPrivacy(_ sender: Any) {
        self.view.endEditing(true)
        let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "WebViewController") as? WebViewController
        vc?.cms_id = Singleton.shared.cms_id_privacy
        self.navigationController?.pushViewController(vc!, animated: true)
    }
    
    @objc func updateData(_ notification: Notification) {
        self.view.endEditing(true)
//        self.tableview.reloadData()
//        self.tableviewHeight.constant = CGFloat(Singleton.shared.arrSubscriptionData.count * 85)
    }
    
    @IBAction func btnMonthly(_ sender: Any) {
        self.view.endEditing(true)
        if(Singleton.shared.subscription_platform == "1"){
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "You can not buy this plan from iOS platform") { (index, title) in }
        } else if (Singleton.shared.subscription_platform == "2" && Singleton.shared.subscription_bundle_id == "com.ar.patient.gold") {
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "You can not purchase monthly subscription since you have already purchased yearly subscription. To downgrade, please wait until the expiration date and the cancel the yearly package and then go for monthly package.") { (index, title) in }
        } else {
            self.purchaseSubscription(tag: 1)
        }
    }
    
    @IBAction func btnYearly(_ sender: Any) {
        self.view.endEditing(true)
        if(Singleton.shared.subscription_platform == "1"){
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "You can not buy this plan from iOS platform") { (index, title) in }
        } else {
            self.purchaseSubscription(tag: 2)
        }
    }
    
    @IBAction func btnManageSub(_ sender: Any) {
        self.view.endEditing(true)
    }
}

//MARK:- Tableview methods
/*extension SubscriptionVC: UITableViewDelegate, UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return Singleton.shared.arrSubscriptionData.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableview.dequeueReusableCell(withIdentifier: "SubscriptionContentCell") as! SubscriptionContentCell
        cell.lblContent.text = Singleton.shared.arrSubscriptionData[indexPath.row]
        cell.selectionStyle = .none
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 90
    }
}*/

//MARK:- API CALLING
extension SubscriptionVC{
    func inAppPurchaseAPI(productID: String, txnDate: Date, currency: String, txnID: String, originalTxnID: String, price: String) {
        var paramer: [String: Any] = [:]
        
        var data = ""
        if let receiptURL = Bundle.main.appStoreReceiptURL{
            if let receipt = NSData(contentsOf: receiptURL){
                data =  receipt.base64EncodedString(options: [])
            }
        }
        paramer["inapp_purchase_id"] = productID
        paramer["transaction_id"] = txnID
        paramer["order_id"] = originalTxnID
        paramer["currency"] = currency
        paramer["startdate"] = txnDate.dateTimeString(withFormate: "yyyy-MM-dd HH:mm:ss")
        paramer["enddate"] = txnDate.add(months: 1).dateTimeString(withFormate: "yyyy-MM-dd HH:mm:ss")
        paramer["data"] = data
        paramer["price"] = price
        paramer["islive"] = Global.appDelegate.isLive
        
        WebService.call.POST(filePath: APIConstant.Request.inAppPurchse, params: paramer, enableInteraction: false, showLoader: true, viewObj: (Global.appDelegate.window?.rootViewController!.view)!, onSuccess: { (result, success) in
            if let Dict = result as? [String:Any] {

                if let str = Dict["code"] as? Int , str == 0{
                    let message = Dict["message"] as! String
                    AJAlertController.initialization().showAlertWithOkButton(aStrMessage: message) { (index, title) in
                        Singleton.shared.subscription_exp_date = JSON(Dict["exp_date"]!).stringValue
                        Singleton.shared.is_subscribed = JSON(Dict["is_subscribed"]!).stringValue
                        Singleton.shared.subscription_bundle_id = productID
                        Global().delay(delay: 0.5, closure: {
                            self.view.endEditing(true)
                            self.navigationController?.popViewController(animated: true)
                        })
                    }
                } else{
                    AJAlertController.initialization().showAlertWithOkButton(aStrMessage: Dict["message"] as? String ?? "") { (index, title) in
                    }
                }
            }
            
        }) {
            print("Error \(self.description)")
        }
    }
}
