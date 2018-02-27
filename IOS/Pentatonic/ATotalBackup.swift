//
//  ATotalBackup.swift
//  Pentatonic
//
//  Created by Armand Hornik on 26/02/2018.
//  Copyright © 2018 Armand Hornik. All rights reserved.
//

import UIKit

class ATotalBackup: NSObject, Codable {
    
    var joueur:String?
    var totale: [ABackup]?
    
    enum CodingKeys:String, CodingKey {
        case joueur
        case totale
    }

}
