//
//  prefsJSON.swift
//  Pentatonic
//
//  Created by Armand Hornik on 04/04/2018.
//  Copyright © 2018 Armand Hornik. All rights reserved.
//

import UIKit

class prefsJSON: NSObject, Codable {
    enum Level:Int , Codable {
        case beginner
        case easy
        case medium
        case hard
        case veryHard
    }
    enum LColor:Int, Codable {
        case lightest
        case lighter
        case light
        case dark
        case darker
        case darkest
        case blue
        case red
        case black
        case completed
        case ongoing
        case empty
    }
    let greyTheme:[LColor:Int] = [LColor.lightest:0xeeeeee,
                                  LColor.lighter:0xaaaaaa,
                                  LColor.light:0x999999,
                                  LColor.dark:0x777777,
                                  LColor.darker:0x555555,
                                  LColor.darkest:0x333333,
                                  LColor.blue:0x0000ff,
                                  LColor.red:0xff0000,
                                  LColor.black:0x000000,
                                  LColor.completed:0x333333,
                                  LColor.ongoing:0x999999,
                                  LColor.empty:0xaaaaaa
    ]
    let blueTheme:[LColor:Int] = [LColor.lightest:0xeeeeff,
                                  LColor.lighter:0xaaaadd,
                                  LColor.light:0x9999cc,
                                  LColor.dark:0x7777aa,
                                  LColor.darker:0x555588,
                                  LColor.darkest:0x333366,
                                  LColor.blue:0x0000ff,
                                  LColor.red:0xff0000,
                                  LColor.black:0x000000,
                                  LColor.completed:0x333366,
                                  LColor.ongoing:0x9999cc,
                                  LColor.empty:0xaaaadd
    ]
    let redTheme:[LColor:Int] = [LColor.lightest:0xffeeee,
                                 LColor.lighter:0xddaaaa,
                                 LColor.light:0xcc9999,
                                 LColor.dark:0xaa7777,
                                 LColor.darker:0x885555,
                                 LColor.darkest:0x663333,
                                 LColor.blue:0x0000ff,
                                 LColor.red:0xff0000,
                                 LColor.black:0x000000,
                                 LColor.completed:0x663333,
                                 LColor.ongoing:0xcc9999,
                                 LColor.empty:0xddaaaa
    ]
    let greenTheme:[LColor:Int] = [LColor.lightest:0xeeffee,
                                   LColor.lighter:0xaaddaa,
                                   LColor.light:0x99cc99,
                                   LColor.dark:0x77aa77,
                                   LColor.darker:0x558855,
                                   LColor.darkest:0x336633,
                                   LColor.blue:0x0000ff,
                                   LColor.red:0xff0000,
                                   LColor.black:0x000000,
                                   LColor.completed:0x336633,
                                   LColor.ongoing:0x99cc99,
                                   LColor.empty:0xaaddaa
        
    ]
    let yustinaTheme:[LColor:Int] = [LColor.lightest:0xfffaea,
                                     
                                     LColor.lighter:0xfff7e6,
                                     LColor.light:0xffe7b3,
                                     LColor.dark:0xffd780,
                                     
                                     LColor.darker:0xeebf65,
                                     LColor.darkest:0xdfa652,
                                     LColor.blue:0xcc8800,
                                     LColor.red:0xff0000,
                                     LColor.black:0x000000,
                                     LColor.completed:0xdfa652,
                                     LColor.ongoing:0xffe7b3,
                                     LColor.empty:0xfff7e6
        //4d1923
    ]
    var currentTheme:[LColor:Int]?
    var currentCol:Int = 0
    var helpButtonValue = false
    var remotePentasGit = false
    var doNotSave = false
    var forceDoNotSave = false
    var zoomScrollActivated = false
    var levelMax = 1
    var oldBehaviour:Bool? = true
    
    enum CodingKeys:String, CodingKey {
        case greyTheme
        case blueTheme
        case redTheme
        case greenTheme
        case yustinaTheme
        case currentTheme
        case currentCol
        case helpButtonValue
        case remotePentasGit
        case doNotSave
        case forceDoNotSave
        case zoomScrollActivated
        case levelMax
        case oldBehaviour
    }
    override init() {
        currentTheme = greyTheme
        super.init()
        
    }
}
