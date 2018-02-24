//
//  rulesViewController.swift
//  Pentatonic
//
//  Created by Armand Hornik on 15/02/2018.
//  Copyright © 2018 Armand Hornik. All rights reserved.
//

import UIKit

class rulesViewController: UIViewController {
    //lazy var tata:Initializor = Initializor.init(name: "Armand", line: 5, column: 5)
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBOutlet var backButton: UIButton!
    
//    @IBAction func readPenta(_ sender: Any) {
//       tata.displayPenta(fileStr: "5Aller_retour.penta")
//        tata.listAllFiles()
//
//
//        var Directory:String = Bundle.main.path(forResource: "logo", ofType: "penta")!
//        var components = Directory.components(separatedBy: "/")
//
//        if components.count > 1 {
//            components.removeLast()
//            Directory = components.joined(separator: "/")
//        }
//        _=tata.listFiles(directory: Directory, suffix: "penta")
//
//
//    }
    @IBAction func backAction(_ sender: Any) {
        dismiss(animated: true, completion: nil)
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
