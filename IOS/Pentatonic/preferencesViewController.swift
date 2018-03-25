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
