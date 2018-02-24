//
//  AValeur.swift
//  Pentatonic
//
//  Created by Armand Hornik on 20/02/2018.
//  Copyright © 2018 Armand Hornik. All rights reserved.
//

import UIKit

class AValeur: NSObject,Codable {
    var val: Int?
    var i: Int?
    var j: Int?
    
    enum CodingKeys:String, CodingKey {
        case val
        case i
        case j
    }
}

