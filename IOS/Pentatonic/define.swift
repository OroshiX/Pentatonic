//
//  define.swift
//  Pentatonic
//
//  Created by Armand Hornik on 15/02/2018.
//  Copyright © 2018 Armand Hornik. All rights reserved.
//

import Foundation
import UIKit

class define:NSObject {
    
    
    enum Level:Int , Codable {
        case beginner
        case easy
        case medium
        case hard
        case veryHard
    }
    
    let greyTheme:[prefsJSON.LColor:UIColor] = [prefsJSON.LColor.lightest:UIColor(rgb:0xeeeeee),
                                  prefsJSON.LColor.lighter:UIColor(rgb:0xaaaaaa),
                                  prefsJSON.LColor.light:UIColor(rgb:0x999999),
                                  prefsJSON.LColor.dark:UIColor(rgb:0x777777),
                                  prefsJSON.LColor.darker:UIColor(rgb:0x555555),
                                  prefsJSON.LColor.darkest:UIColor(rgb:0x333333),
                                  prefsJSON.LColor.blue:UIColor(rgb:0x0000ff),
                                  prefsJSON.LColor.red:UIColor(rgb:0xff0000),
                                  prefsJSON.LColor.black:UIColor(rgb:0x000000),
                                  prefsJSON.LColor.completed:UIColor(rgb:0x333333),
                                  prefsJSON.LColor.ongoing:UIColor(rgb:0x999999),
                                  prefsJSON.LColor.empty:UIColor(rgb:0xaaaaaa)
    ]
    let blueTheme:[prefsJSON.LColor:UIColor] = [prefsJSON.LColor.lightest:UIColor(rgb:0xeeeeff),
                                      prefsJSON.LColor.lighter:UIColor(rgb:0xaaaadd),
                                      prefsJSON.LColor.light:UIColor(rgb:0x9999cc),
                                      prefsJSON.LColor.dark:UIColor(rgb:0x7777aa),
                                      prefsJSON.LColor.darker:UIColor(rgb:0x555588),
                                      prefsJSON.LColor.darkest:UIColor(rgb:0x333366),
                                      prefsJSON.LColor.blue:UIColor(rgb:0x0000ff),
                                      prefsJSON.LColor.red:UIColor(rgb:0xff0000),
                                      prefsJSON.LColor.black:UIColor(rgb:0x000000),
                                      prefsJSON.LColor.completed:UIColor(rgb:0x333366),
                                      prefsJSON.LColor.ongoing:UIColor(rgb:0x9999cc),
                                      prefsJSON.LColor.empty:UIColor(rgb:0xaaaadd)
    ]
    let redTheme:[prefsJSON.LColor:UIColor] = [prefsJSON.LColor.lightest:UIColor(rgb:0xffeeee),
                                     prefsJSON.LColor.lighter:UIColor(rgb:0xddaaaa),
                                     prefsJSON.LColor.light:UIColor(rgb:0xcc9999),
                                     prefsJSON.LColor.dark:UIColor(rgb:0xaa7777),
                                     prefsJSON.LColor.darker:UIColor(rgb:0x885555),
                                     prefsJSON.LColor.darkest:UIColor(rgb:0x663333),
                                     prefsJSON.LColor.blue:UIColor(rgb:0x0000ff),
                                     prefsJSON.LColor.red:UIColor(rgb:0xff0000),
                                     prefsJSON.LColor.black:UIColor(rgb:0x000000),
                                     prefsJSON.LColor.completed:UIColor(rgb:0x663333),
                                     prefsJSON.LColor.ongoing:UIColor(rgb:0xcc9999),
                                     prefsJSON.LColor.empty:UIColor(rgb:0xddaaaa)
    ]
    let greenTheme:[prefsJSON.LColor:UIColor] = [prefsJSON.LColor.lightest:UIColor(rgb:0xeeffee),
                                       prefsJSON.LColor.lighter:UIColor(rgb:0xaaddaa),
                                       prefsJSON.LColor.light:UIColor(rgb:0x99cc99),
                                       prefsJSON.LColor.dark:UIColor(rgb:0x77aa77),
                                       prefsJSON.LColor.darker:UIColor(rgb:0x558855),
                                       prefsJSON.LColor.darkest:UIColor(rgb:0x336633),
                                       prefsJSON.LColor.blue:UIColor(rgb:0x0000ff),
                                       prefsJSON.LColor.red:UIColor(rgb:0xff0000),
                                       prefsJSON.LColor.black:UIColor(rgb:0x000000),
                                       prefsJSON.LColor.completed:UIColor(rgb:0x336633),
                                       prefsJSON.LColor.ongoing:UIColor(rgb:0x99cc99),
                                       prefsJSON.LColor.empty:UIColor(rgb:0xaaddaa)

    ]
    let yustinaTheme:[prefsJSON.LColor:UIColor] = [prefsJSON.LColor.lightest:UIColor(rgb:0xfffaea),
                                         
                                         prefsJSON.LColor.lighter:UIColor(rgb:0xfff7e6),
                                         prefsJSON.LColor.light:UIColor(rgb:0xffe7b3),
                                         prefsJSON.LColor.dark:UIColor(rgb:0xffd780),
                                         
                                         prefsJSON.LColor.darker:UIColor(rgb:0xeebf65),
                                         prefsJSON.LColor.darkest:UIColor(rgb:0xdfa652),
                                         prefsJSON.LColor.blue:UIColor(rgb:0xcc8800),
                                         prefsJSON.LColor.red:UIColor(rgb:0xff0000),
                                         prefsJSON.LColor.black:UIColor(rgb:0x000000),
                                         prefsJSON.LColor.completed:UIColor(rgb:0xdfa652),
                                         prefsJSON.LColor.ongoing:UIColor(rgb:0xffe7b3),
                                         prefsJSON.LColor.empty:UIColor(rgb:0xfff7e6)
        //4d1923
    ]
    
    
    let level:[Level:String] = [ Level.beginner:"beginner",
                                 Level.easy:"Easy",
                                 Level.medium:"Medium",
                                 Level.hard:"Hard",
                                 Level.veryHard:"Very_Hard"]
    
    let allLevel:[Level] = [Level.beginner,Level.easy,Level.medium,Level.hard,Level.veryHard]
    var currentTheme:[prefsJSON.LColor:UIColor]
    var currentCol:Int = 0
    var helpButtonValue = false
    var remotePentasGit = false
    var doNotSave = false
    var forceDoNotSave = false
    var zoomScrollActivated = false
    var levelMax = 1
    enum CodingKeys:String, CodingKey {
        case greyTheme
        case blueTheme
        case redTheme
        case greenTheme
        case yustinaTheme
        case level
        case allLevel
        case currentTheme
        case currentCol
        case helpButtonValue
        case remotePentasGit
        case doNotSave
        case forceDoNotSave
        case zoomScrollActivated
        case levelMax
    }
    override init() {
        currentTheme = greyTheme
        super.init()

    }
}
