//
//  ABackup.swift
//  Pentatonic
//
//  Created by Armand Hornik on 26/02/2018.
//  Copyright © 2018 Armand Hornik. All rights reserved.
//

import UIKit

class ABackup: NSObject, Codable {
    var name:String?
    var completed:Bool?
    var vSet :[Set<Int>]?
    
    enum CodingKeys:String, CodingKey {
        case vSet
        case name
        case completed
    }
}
