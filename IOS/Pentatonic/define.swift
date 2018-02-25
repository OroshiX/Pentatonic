//
//  define.swift
//  Pentatonic
//
//  Created by Armand Hornik on 15/02/2018.
//  Copyright © 2018 Armand Hornik. All rights reserved.
//

import Foundation
import UIKit

struct define {
    
    
    enum Level {
        case beginner
        case easy
        case medium
        case hard
        case veryHard
    }
    enum LColor {
        case lightest
        case lighter
        case light
        case dark
        case darker
        case darkest
    }
    let greyTheme:[LColor:UIColor] = [LColor.lightest:UIColor(rgb:0xeeeeee),
                                  LColor.lighter:UIColor(rgb:0xaaaaaa),
                                  LColor.light:UIColor(rgb:0x999999),
                                  LColor.dark:UIColor(rgb:0x777777),
                                  LColor.darker:UIColor(rgb:0x555555),
                                  LColor.darkest:UIColor(rgb:0x333333)
    ]
    let blueTheme:[LColor:UIColor] = [LColor.lightest:UIColor(rgb:0xeeeeff),
                                      LColor.lighter:UIColor(rgb:0xaaaadd),
                                      LColor.light:UIColor(rgb:0x9999cc),
                                      LColor.dark:UIColor(rgb:0x7777aa),
                                      LColor.darker:UIColor(rgb:0x555588),
                                      LColor.darkest:UIColor(rgb:0x333366)
    ]
    let redTheme:[LColor:UIColor] = [LColor.lightest:UIColor(rgb:0xffeeee),
                                     LColor.lighter:UIColor(rgb:0xddaaaa),
                                     LColor.light:UIColor(rgb:0xcc9999),
                                     LColor.dark:UIColor(rgb:0xaa7777),
                                     LColor.darker:UIColor(rgb:0x885555),
                                     LColor.darkest:UIColor(rgb:0x663333)
    ]
    let greenTheme:[LColor:UIColor] = [LColor.lightest:UIColor(rgb:0xeeffee),
                                       LColor.lighter:UIColor(rgb:0xaaddaa),
                                       LColor.light:UIColor(rgb:0x99cc99),
                                       LColor.dark:UIColor(rgb:0x77aa77),
                                       LColor.darker:UIColor(rgb:0x558855),
                                       LColor.darkest:UIColor(rgb:0x336633)
    ]
    let level:[Level:String] = [ Level.beginner:"beginner",
                                 Level.easy:"Easy",
                                 Level.medium:"Medium",
                                 Level.hard:"Hard",
                                 Level.veryHard:"Very_Hard"]
    
    let allLevel:[Level] = [Level.beginner,Level.easy,Level.medium,Level.hard,Level.veryHard]
    var currentTheme:[LColor:UIColor]
    var currentCol:Int = 0
    var helpButtonValue = false
    
    init() {
        currentTheme = greyTheme
    }
}
