//
//  listNiveauViewController.swift
//  Pentatonic
//
//  Created by Armand Hornik on 06/01/2018.
//  Copyright © 2018 Armand Hornik. All rights reserved.
//

import UIKit

class listNiveauViewController: UIViewController {

    var arrayLevelButtons:[UIButton] = []
    var pentas:[APenta] = []
    
    @IBOutlet var imageView: UIImageView!
    @IBOutlet var labelCurrentLevel: UILabel!
    @IBOutlet var difficultySegment: UISegmentedControl!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        // Do any additional setup after loading the view.
        /* Read the JSON File with all level available */
        
        pentas = readJson()
        // displayPentas(pentas)
        
        // Initialize the data struct containing all levels
        
        for level in ldefine.allLevel
        {
            arrayLevels[level] = []
        }
        
        for level in pentas {
            if let value = level.difficulty {
                arrayLevels[ldefine.allLevel[value-1]]?.append(level.name!)
            }
        }
        // Create the table of buttons
        createButtons()
        // Loading the available Levels
        let segment = difficultySegment.selectedSegmentIndex
        
        let currentDifficulty = ldefine.allLevel[segment]

        let currentMaxLevel = (arrayLevels[currentDifficulty]?.count)!
        displayButtons(currentMaxLevel)
        print ("Levels loaded : \(arrayLevels)")
        labelCurrentLevel.text = ""
    }
    
    func createButtons() {
        let screenSize: CGRect = UIScreen.main.bounds
        let screenWidth = screenSize.width
        let screenHeight = screenSize.height
        let minW:Int
        if screenWidth < screenHeight { minW = Int(screenWidth)} else { minW = Int(screenHeight)}
        
        let tableImage:[UIImage] = [UIImage(named: "rect_rose.png")!,UIImage(named: "rect_jaune.png")!,UIImage(named: "rect_turq.png")!]
        var widthBut:Int
        var heightBut:Int
        var initialX:Int
        var initialY:Int
        let NBButton = 8
        
        let divSize:Int = NBButton + 2
        widthBut = minW / divSize
        heightBut = widthBut
        initialX = widthBut
        initialY = heightBut*2
        
        for j in 0..<NBButton-1 {
            for i in 0..<NBButton {
                let button = UIButton(frame: CGRect(x: initialX+i*widthBut, y: initialY+j*heightBut, width: widthBut, height: heightBut))
                button.backgroundColor = .green
                button.setTitle("button\(i)", for: [])
                button.setImage(tableImage[(i*NBButton+j)%tableImage.count], for: [])
                button.addTarget(self, action: #selector(buttonAction), for: .touchUpInside)
                button.tag = j*NBButton+i
                arrayLevelButtons.append(button)
                self.view.addSubview(button)
                
                
            }
        }

    }
    
    func displayDebugPentas(_ pentas:[APenta]) {
        for p in pentas {
            print ("\n\nuser:\(p.author!) name:\(p.name!)")
            print ("width:\(p.width!) length:\(p.height!) difficulty: \(p.difficulty!)")
            for data in p.data! {
                print ("dataXY = \(data)")
                
            }
            for valeur in p.valeurs! {
                print ("valeurI = \(valeur.i!) - valeur of type \(type(of: valeur))")
                
            }
            for sister in p.sisters! {
                print ("\n----sister \(sister.id!)")
                for point in sister.positions! {
                    print (" in \(sister.id!) : point= \(point.i!),\(point.j!)")
                }
                
            }
            if (p.differences != nil) {
                for diff in p.differences! {
                    print ("diff = \(diff)")
                    print ("diff.point1 = \(diff.point1!)")
                    print ("----Difference entre 1 {\(diff.point1!.i!),\(diff.point1!.j!)} et {\(diff.point2!.i!),\(diff.point2!.j!)}")
                }
            }
        }
    }
    func readJson() -> [APenta]{
        let url = URL(fileURLWithPath: Bundle.main.path(forResource: "all", ofType: "json")!)
        let jsonTableData = try? Data(contentsOf:url)
        let decoder = JSONDecoder()
        let pentas = try! decoder.decode([APenta].self,from: jsonTableData!)
        
        return pentas
        
    }
    func displayButtons(_ nombre:Int) {
        var i:Int = 0
        
        for button in arrayLevelButtons {
            if (i<nombre) {
                button.isHidden = false
            } else {
                button.isHidden = true
            }
            i=i+1
        }
    }
  
    @IBAction func buttonAction(_ sender: UIButton) {
        print("Click on Button \(sender.tag)")
        let segment = difficultySegment.selectedSegmentIndex
        let difficulty = ldefine.allLevel[segment]
        var currentLevel:Int = sender.tag
        
        print("Difficulty = \(segment)")
        print("Current Difficulty \(ldefine.allLevel[segment])")
        let listLevels:[String] = arrayLevels[difficulty]!
        let maxLevel = listLevels.count
        
        if currentLevel >= maxLevel {
            currentLevel = maxLevel - 1
        }
        print ("\(arrayLevels[difficulty]![currentLevel])")
        labelCurrentLevel.text = arrayLevels[difficulty]![currentLevel]
        let aPenta:Initializor = Initializor.init(name: "Armand", line: 0, column: 0)

        var penta:APenta? = nil
        for anyPenta in pentas {
            if anyPenta.name == arrayLevels[difficulty]![currentLevel] {
                penta = anyPenta
                break
            }
        }
        let next:playViewController = playViewController()
        next.setPenta(penta!)
        present(next, animated: true, completion: nil)
    }
    
    
    @IBAction func changeDifficultySegment(_ sender: UISegmentedControl) {
        let segment = sender.selectedSegmentIndex

        let currentDifficulty = ldefine.allLevel[segment]
        let currentMaxLevel = (arrayLevels[currentDifficulty]?.count)!
        displayButtons(currentMaxLevel)
        labelCurrentLevel.text = ""
    }
    @IBAction func backAction(_ sender: Any) {
        dismiss(animated: true, completion: nil)
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



