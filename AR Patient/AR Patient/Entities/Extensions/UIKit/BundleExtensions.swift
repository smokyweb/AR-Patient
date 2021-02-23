//
//  BundleExtensions.swift
//  FoodPon_User
//
//  Created by Ilesh's  on 13/03/19.
//

import Foundation

extension Bundle {
    var displayName: String? {
        return object(forInfoDictionaryKey: "CFBundleDisplayName") as? String
    }
}
