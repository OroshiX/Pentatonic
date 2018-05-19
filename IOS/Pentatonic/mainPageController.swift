//
//  mainPageController.swift
//  Pentatonic
//
//  Created by Armand Hornik on 24/02/2018.
//  Copyright © 2018 Armand Hornik. All rights reserved.
//

import UIKit
var arrayLevels:[define.Level:[String]] = [:]
var ldefine:define = define.init()


extension String {
    
    subscript (_ i: Int) -> Character {
        return self[index(startIndex, offsetBy: i)]
    }
    
    subscript (_ i: Int) -> String {
        return String(self[i] as Character)
    }
    
    subscript (_ r: Range<Int>) -> String {
        let start = index(startIndex, offsetBy: r.lowerBound)
        let end = index(startIndex, offsetBy: r.upperBound)
        return String(self[Range(start ..< end)])
    }
}

class mainPageController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        loadPreferences()
        // Do any additional setup after loading the view.
        
    }

    @IBOutlet var backgroundImage: UIImageView!
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func loadPreferences ()
    {
        let paths = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)
        let myDataPath = paths[0].appending("/preferences.json")
        
        
        /*
         *
         * This function will load preferences from last run ( theme choice, etc ... )
         *
         */
        
        
        
        
        
       
        
        
        
        let url = URL(fileURLWithPath:myDataPath)
        let jsonPrefs = try? Data(contentsOf:url)
        let decoder = JSONDecoder()
        var l:prefsJSON
        if jsonPrefs != nil {
            l = try! decoder.decode(prefsJSON.self,from: jsonPrefs!)
        } else {
             l = prefsJSON()
        }
        
        ldefine.currentCol = l.currentCol
        ldefine.doNotSave = l.doNotSave
        ldefine.currentCol = l.currentCol
        ldefine.helpButtonValue = l.helpButtonValue
        ldefine.remotePentasGit = l.remotePentasGit
        ldefine.doNotSave = l.doNotSave
        ldefine.forceDoNotSave = l.forceDoNotSave
        ldefine.zoomScrollActivated = l.zoomScrollActivated
        ldefine.levelMax = l.levelMax
        ldefine.oldBehaviour = l.oldBehaviour!
        ldefine.currentTheme = [:]
        for col in l.currentTheme! {
            ldefine.currentTheme[col.key] = UIColor(rgb:UInt(col.value))
        }
        
        

        
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
