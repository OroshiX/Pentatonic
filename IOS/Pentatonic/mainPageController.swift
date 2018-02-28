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

//Temporary code to be removed - just to test
var tempPenta:APenta? = nil

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

        // Do any additional setup after loading the view.
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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
