//
//  ADifference.swift
//  Pentatonic
//
//  Created by Armand Hornik on 20/02/2018.
//  Copyright © 2018 Armand Hornik. All rights reserved.
//

import UIKit

class ADifference: NSObject,Codable {
    var point1: APoint?
    var point2: APoint?
    
    enum CodingKeys:String, CodingKey {
        case point1
        case point2
    }
}
