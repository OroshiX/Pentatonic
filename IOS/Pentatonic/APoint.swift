//
//  APoint.swift
//  Pentatonic
//
//  Created by Armand Hornik on 20/02/2018.
//  Copyright © 2018 Armand Hornik. All rights reserved.
//

import UIKit

class APoint: NSObject,Codable {
    var i: Int?
    var j: Int?
    
    enum CodingKeys:String,CodingKey {
        case i
        case j
    }
}


