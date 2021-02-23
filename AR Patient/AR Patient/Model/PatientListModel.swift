//
//  PatientListModel.swift
//  AR Patient
//
//  Created by Krupali on 30/04/20.
//  Copyright © 2020 Silicon. All rights reserved.
//

import Foundation

class PatientListModel: NSObject{
    var avatar : String = ""
    var model : String = ""
    var name : String = ""
    var patient_id : String = ""
    var patient_name : String = ""
    var pt_name : String = ""
    var symptoms : String = ""
    var physical_exam_counter : Int = 0
    var voice_exam_counter : Int = 0
    var vital_signs = [VitalSignModel]()
    var arrBodyParts = [BodyPartsModel]()
    var hint_text : String = ""
}
