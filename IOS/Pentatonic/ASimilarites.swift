//
//  ASimilarites.swift
//  Pentatonic
//
//  Created by Armand Hornik on 20/02/2018.
//  Copyright © 2018 Armand Hornik. All rights reserved.
//

import UIKit

class ASimilarites: NSObject,Codable {
    var id:Int?
    var positions:[APoint]?
    
    enum CodingKeys:String, CodingKey {
        case positions
        case id
    }
}
