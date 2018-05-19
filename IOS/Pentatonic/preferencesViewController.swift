//
//  preferencesViewController.swift
//  Pentatonic
//
//  Created by Armand Hornik on 24/02/2018.
//  Copyright © 2018 Armand Hornik. All rights reserved.
//

import UIKit
var globalUserGameData:ATotalBackup = ATotalBackup()

class preferencesViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        themeSegment.selectedSegmentIndex = ldefine.currentCol
        helpValueSwitch.isOn = ldefine.helpButtonValue
        remotePentaGitSwitch.isOn = ldefine.remotePentasGit
        saveDataGame.isOn = ldefine.forceDoNotSave || ldefine.doNotSave
        zoomScrollSwitch.isOn = ldefine.zoomScrollActivated
        recursSlider.value = Float(ldefine.levelMax)
        labelRecursivity.text = "Level of recursivity " + "\(ldefine.levelMax)"
        oldSwitch.isOn = ldefine.oldBehaviour
        view.backgroundColor = ldefine.currentTheme[prefsJSON.LColor.lightest]
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    @IBOutlet var themeSegment: UISegmentedControl!
    @IBOutlet var helpValueSwitch: UISwitch!
    @IBOutlet var remotePentaGitSwitch: UISwitch!
    @IBOutlet var saveDataGame: UISwitch!
    @IBOutlet var zoomScrollSwitch: UISwitch!
    @IBOutlet var recursSlider: UISlider!
    @IBOutlet var oldSwitch: UISwitch!

    @IBOutlet var labelRecursivity: UILabel!
    @IBAction func backAction(_ sender: Any) {
        dismiss(animated: true, completion: nil)
    }
    
    @IBAction func sliderAction(_ sender: UISlider) {
            ldefine.levelMax = Int(sender.value)
            labelRecursivity.text = "Level of recursivity " + "\(ldefine.levelMax)"
    }
    
    @IBAction func choosingValueAction(_ sender: UISwitch) {
        switch sender.tag {
        case 0:
            ldefine.helpButtonValue = sender.isOn
            if sender.isOn {
                ldefine.forceDoNotSave = true
                saveDataGame.isOn = true
            }
            
        case 1:
            ldefine.remotePentasGit = sender.isOn
        case 2:
            if !ldefine.helpButtonValue {
            ldefine.forceDoNotSave = sender.isOn
            } else {
                sender.isOn = true
                
            }
        case 3:
            ldefine.zoomScrollActivated = sender.isOn
        case 4:
            ldefine.oldBehaviour = sender.isOn
            
        default:
            print("This is a bug")
            
        }
    }
    
    @IBAction func themeSegmentAction(_ sender: UISegmentedControl) {
        let segment = sender.selectedSegmentIndex
        ldefine.currentCol = segment

        switch segment {
        case 0:
            ldefine.currentTheme = ldefine.greyTheme
        case 1:
            ldefine.currentTheme = ldefine.blueTheme
        case 2:
            ldefine.currentTheme = ldefine.redTheme
        case 3:
            ldefine.currentTheme = ldefine.greenTheme
        case 4:
            ldefine.currentTheme = ldefine.yustinaTheme
        default:
            ldefine.currentTheme = ldefine.greyTheme
        }
        view.backgroundColor = ldefine.currentTheme[prefsJSON.LColor.lightest]

    }
    override func viewWillDisappear(_ animated: Bool) {
        savePreferences()
    }
    func savePreferences ()
    {
        let paths = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)
        let myDataPath = paths[0].appending("/preferences.json")
        
        
        /*
         * This function save most of the parameters locally
         * Themes choosen, etc ...
         */
        let url = URL(fileURLWithPath:myDataPath)
        let l:prefsJSON = prefsJSON()
        l.currentCol = ldefine.currentCol
        l.doNotSave = ldefine.doNotSave
        l.currentCol = ldefine.currentCol
        l.helpButtonValue = ldefine.helpButtonValue
        l.remotePentasGit = ldefine.remotePentasGit
        l.doNotSave = ldefine.doNotSave
        l.forceDoNotSave = ldefine.forceDoNotSave
        l.zoomScrollActivated = ldefine.zoomScrollActivated
        l.levelMax = ldefine.levelMax
        l.currentTheme = [:]
        l.oldBehaviour = ldefine.oldBehaviour
        for col in ldefine.currentTheme {
            l.currentTheme![col.key] = rgb(color: col.value)
        }
        let encodedData = try? JSONEncoder().encode(l)
        try? encodedData?.write(to: url)

    }

    //    Functions used to calculate RGB classical value ( in 0xRRGGBB format)
    func rgb(color:UIColor) -> Int {
        let _components = color.cgColor.components
        let red     = _components![0]
        let green = _components![1]
        let blue   = _components![2]
        //let alpha = _components![3]
        let r = (Int(red*255.0) << 16)
        let g = (Int(green*255.0) << 8)
        let b = Int(blue*255.0)
        let rgb = String(r+g+b, radix:16)
        print("RGB = \(rgb)")
        return r+g+b
    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
     // Pass the selvared object to the new view controller.
    }
    */

}
