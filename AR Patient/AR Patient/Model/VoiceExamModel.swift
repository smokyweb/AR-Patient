//
//  VoiceExamModel.swift
//  AR Patient
//
//  Created by Knoxweb on 02/05/20.
//  Copyright © 2020 Knoxweb. All rights reserved.
//

import UIKit

class VoiceExamModel: Encodable {
    
    var answer : String = ""
    var id : String = ""
    var question : String = ""
    var is_mandatory : String = ""
}

class ChatModel: NSObject {
    
    var isMe : Bool = true
    var message : String = ""
}
