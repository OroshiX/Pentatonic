//
//  ADifference.swift
//  Pentatonic
//
//  Created by Armand Hornik on 20/02/2018.
//  Copyright © 2018 Armand Hornik. All rights reserved.
//

import UIKit

class ADifference: NSObject,Codable {
    var position1: APoint?
    var position2: APoint?
    
    enum CodingKeys:String, CodingKey {
        case position1
        case position2
    }
}
