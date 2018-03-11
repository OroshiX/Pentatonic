//
//  APenta.swift
//  Pentatonic
//
//  Created by Armand Hornik on 20/02/2018.
//  Copyright © 2018 Armand Hornik. All rights reserved.
//

import UIKit

class APenta: NSObject, Codable {
    var name:String?
    var author:String?
    var width:Int?
    var height:Int?
    var difficulty:Int?
    var data:[ [String]]?
    var values: Set<AValeur>?
    var sisters:[ASimilarites]?
    var differences:[ADifference]?
    
    enum CodingKeys:String, CodingKey {
        case name
        case author
        case width
        case height
        case difficulty
        case data
        case values
        case sisters
        case differences
    }
}


