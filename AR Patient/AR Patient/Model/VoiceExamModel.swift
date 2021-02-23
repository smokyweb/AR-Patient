//
//  VoiceExamModel.swift
//  AR Patient
//
//  Created by Silicon on 02/05/20.
//  Copyright © 2020 Silicon. All rights reserved.
//

import UIKit

class VoiceExamModel: Encodable {
    
    var answer : String = ""
    var id : String = ""
    var question : String = ""
}

class ChatModel: NSObject {
    
    var isMe : Bool = true
    var message : String = ""
}
