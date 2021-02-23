//
//  AppTheme.swift
//  FoodPon_User
//
//  Created by Ilesh's  on 14/03/19.
//

import UIKit

class Theme: NSObject {
    
    struct app{
        static let name = "AR Patient"
    }
    // MARK: -  Application Colors
    struct color {
        
        static let primaryTheme = #colorLiteral(red: 1, green: 0.4196078431, blue: 0.2509803922, alpha: 1) //ff6b40
        static let primaryRed = #colorLiteral(red: 0.9568627451, green: 0.2705882353, blue: 0.2745098039, alpha: 1) //#f44546
        
        // UIVIEWS AND BUTTONS
        //static let primaryGreen = #colorLiteral(red: 0.06666666667, green: 0.9607843137, blue: 0.737254902, alpha: 1) //11f5bc
                
        // LABELS AND TEXTS
        static let text = #colorLiteral(red: 0.2509803922, green: 0.2509803922, blue: 0.2588235294, alpha: 1) //404042
        static let textPlaceholder = #colorLiteral(red: 0.7411764706, green: 0.7450980392, blue: 0.7529411765, alpha: 1) //bdbec0
        
        static let textDark = #colorLiteral(red: 0.6039215686, green: 0.6039215686, blue: 0.6039215686, alpha: 1) //9A9A9A
        static let textPlaceholderDark = #colorLiteral(red: 0.6039215686, green: 0.6039215686, blue: 0.6039215686, alpha: 1) //9A9A9A
        static let textShadow = #colorLiteral(red: 0.6039215686, green: 0.6039215686, blue: 0.6039215686, alpha: 1) //9A9A9A
        
        static let btnText = #colorLiteral(red: 1, green: 1, blue: 1, alpha: 1) //ffffff
        static let btn1BG = #colorLiteral(red: 0, green: 0.7176470588, blue: 0.768627451, alpha: 1) //00B7C4 //2BD6E3
        static let btn2BG = #colorLiteral(red: 0, green: 0.09803921569, blue: 0.1294117647, alpha: 1) //001921 //19323E
        static let lightBlueBG = #colorLiteral(red: 0.9294117647, green: 0.9921568627, blue: 0.9921568627, alpha: 1) //EDFDFD
        static let lightBlueBorder = #colorLiteral(red: 0.6431372549, green: 0.9058823529, blue: 0.937254902, alpha: 1) //A4E7EF
        
        // TEXTVIEW BG
        static let textFieldBG = #colorLiteral(red: 1, green: 1, blue: 1, alpha: 1) //152a3b 50%
        static let textFieldLightBG = #colorLiteral(red: 0.9490196078, green: 0.9490196078, blue: 0.9490196078, alpha: 1) //152a3b 50%
        static let textFieldBG2 = #colorLiteral(red: 0.9568627451, green: 0.9568627451, blue: 0.9568627451, alpha: 1) //F4F4F4
        
        static let GreenColor =  #colorLiteral(red: 0.4666666687, green: 0.7647058964, blue: 0.2666666806, alpha: 1)
        static let RedColor =  #colorLiteral(red: 1, green: 0.08235294118, blue: 0.09803921569, alpha: 1)
        static let BlueColor =  #colorLiteral(red: 0.2588235294, green: 0.5215686275, blue: 0.9568627451, alpha: 1)
        static let BlackColor = #colorLiteral(red: 0, green: 0, blue: 0, alpha: 1)
        static let OrangeColor = #colorLiteral(red: 0.9529411793, green: 0.6862745285, blue: 0.1333333403, alpha: 1)
    }
    
    // MARK: - Application Fonts
    struct Font {
        
        static let Light = "SFUIText-Light"
//      static let ExtraLight = "Poppins-ExtraLight"
//      static let Black = "Poppins-Black"
        static let Bold  = "Poppins-Bold"
        static let SemiBold  = "Poppins-SemiBold"
        static let ExtraBold  = "Poppins-ExtraBold"
        static let Regular = "Poppins-Regular"
//      static let Thin  = "Poppins-Thin"
        static let Medium = "Poppins-Medium"
        static let Title = "PlayfairDisplay-Black"
        static let robotoRegular = "Roboto-Regular"
        
        struct size {
            static let TxtSmallSize_8:CGFloat = 8
            static let TxtSize:CGFloat = 14
            static let TxtSize_13:CGFloat = 13
            static let TxtSize_12:CGFloat = 12
            static let TxtSize_10:CGFloat = 10
            static let TxtSize_14:CGFloat = 14
            static let TxtSize_11:CGFloat = 11
            static let TxtSize_9:CGFloat = 09
            static let TxtSize_28:CGFloat = 28
            
            static let TopHeaderSize:CGFloat = 17
            static let TitleSize:CGFloat = 18
            static let TitleSize_30:CGFloat = 30
            static let TitleSize_23:CGFloat = 23
            
            static let btnSize:CGFloat = 16
            static let lblSize:CGFloat = 15
            
            static let appNameSize:CGFloat = 40
        }
    }
}


struct Google {
    struct maps {
        static let keys = "AIzaSyBelqKx1Bih5mj-ijy3SZip8ksEpjE7zMg" // GIVEN BY UMANG SIR:- 09/08/2019
    }
    
    struct firebase {
        static let serverKey = "AAAAz6-TaR4:APA91bEuBrPENFSzNTfeZKw2WfxCLKpao3YcqydHCLG5kq6GfIJWo1cnu6QYWa5ok3dzR-V_titJQoFoJbCzmIFFf4ZmtKjTkzlpZPI3adAcwL4JcZBD6alfgux4lFZitfqkH9C67bSt"
        
        struct table {
            static let chat = "chat"
        }
    }
}
