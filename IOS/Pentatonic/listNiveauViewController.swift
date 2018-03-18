//
//  listNiveauViewController.swift
//  Pentatonic
//
//  Created by Armand Hornik on 06/01/2018.
//  Copyright © 2018 Armand Hornik. All rights reserved.
//

import UIKit

var incrementPenta:Int = 0
var incrementYPenta:Int = 0

class listNiveauViewController: UIViewController {

    var arrayLevelButtons:[UIButton] = []
    var pentas:[APenta] = []
    var butWidthO:CGFloat = 0
    var butHeightO:CGFloat = 0
    
    @IBOutlet var labelIncrement: UILabel!
    @IBOutlet var labelYIncrement: UILabel!
    @IBOutlet var imageView: UIImageView!
    @IBOutlet var labelCurrentLevel: UILabel!
    @IBOutlet var difficultySegment: UISegmentedControl!
    @IBOutlet var backgroundImage: UIImageView!
    
    @IBOutlet var sliderYIncrement: UISlider!
    @IBOutlet var sliderIncrement: UISlider!
    override func viewDidLoad() {
        super.viewDidLoad()
        
        /* Debug use only - so dont display it */
        sliderIncrement.value = Float(incrementPenta)
        labelIncrement.text = "X\(incrementPenta)"
        
        sliderYIncrement.value = Float(incrementYPenta)
        labelYIncrement.text = "Y\(incrementYPenta)"

            sliderIncrement.isHidden = true
        sliderYIncrement.isHidden = true
        labelIncrement.isHidden = true
        labelYIncrement.isHidden = true
        
        // Do any additional setup after loading the view.
        /* Read the JSON File with all level available */
        
        pentas = readJson()
        // displayPentas(pentas)
        
        globalUserGameData = readBackup()
        
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
        labelCurrentLevel.text = ""
        let swipeRight = UISwipeGestureRecognizer(target: self, action: #selector(respondToSwipeGesture))
        swipeRight.direction = UISwipeGestureRecognizerDirection.right
        self.view.addGestureRecognizer(swipeRight)
        
        let swipeLeft = UISwipeGestureRecognizer(target: self, action: #selector(respondToSwipeGesture))
        swipeLeft.direction = UISwipeGestureRecognizerDirection.left
        self.view.addGestureRecognizer(swipeLeft)
    }
    
    @IBAction func incrementYAction(_ sender: UISlider) {
        incrementYPenta = Int(sender.value)
        sender.value = Float(incrementYPenta)
        labelYIncrement.text = "Y\(incrementYPenta)"

    }
    @IBAction func incrementAction(_ sender: UISlider) {
        incrementPenta = Int(sender.value)
        sender.value = Float(incrementPenta)
        labelIncrement.text = "X\(incrementPenta)"
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
        let NBButtonH = 4
        let NBButtonV = 6

        let divSize:Int = NBButtonH + 1
        widthBut = minW / divSize
        heightBut = widthBut
        initialX = widthBut/4
        initialY = heightBut/2
        
        var tagA:Int = -1
        for j in 0..<NBButtonV-1 {
            for i in 0..<NBButtonH {
                tagA = tagA+1
                butWidthO = CGFloat(widthBut)
                butHeightO = CGFloat(heightBut)
                let button = UIButton(frame: CGRect(x: initialX+i*widthBut, y: initialY+j*heightBut, width: widthBut, height: heightBut))
                //button.backgroundColor = .green
                //button.setTitle("button\(i)", for: [])
                button.setImage(tableImage[(i*NBButtonH+j)%tableImage.count], for: [])
                button.addTarget(self, action: #selector(buttonAction), for: .touchUpInside)
                button.tag = tagA
                arrayLevelButtons.append(button)
                self.view.addSubview(button)
                
                
            }
        }

    }
    
    func readJson() -> [APenta]{
        var url:URL
        if ldefine.remotePentasGit {
            url = URL(string:"https://raw.githubusercontent.com/OroshiX/Pentatonic/master/IOS/Pentatonic/Datas/all.json")!
            
        } else {
            url = URL(fileURLWithPath: Bundle.main.path(forResource: "all", ofType: "json")!)
        }
        let jsonTableData = try? Data(contentsOf:url)
        var pentas:[APenta] = []
        if jsonTableData != nil {
            let decoder = JSONDecoder()
            
            pentas = try! decoder.decode([APenta].self,from: jsonTableData!)
            ldefine.doNotSave = false
        } else {
            // If the pentas were not read correctly, it's probably safer not to save anything ...
            ldefine.doNotSave = true
            
        }
        return pentas
        
    }
    func readBackup() -> ATotalBackup {
        
        let paths = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)
        let myDataPath = paths[0].appending("/globalUserData.json")
        
        let url = URL(fileURLWithPath:myDataPath)
        
        let jsonTotalBackup = try? Data(contentsOf:url)
        let decoder = JSONDecoder()
        if jsonTotalBackup != nil {
            let theBackup = try! decoder.decode(ATotalBackup.self,from: jsonTotalBackup!)
            return theBackup
        } else {
            let theBackup:ATotalBackup = ATotalBackup()
            theBackup.joueur = "Armand"
            theBackup.totale = []
            return theBackup
            
        }
    }
    
    
    func saveUploadedFilesSet(fileName:[String : Any]) {
        let paths = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)
        let myDataPath = paths[0].appending("/toto.json")
        print (myDataPath)
        
        let file: FileHandle? = FileHandle(forWritingAtPath: myDataPath)
        
        if file != nil {
            // Set the data we want to write
            
            do {
                let jsonData = try JSONSerialization.data(withJSONObject: fileName,
                                                          options: .init(rawValue: 0))
                // Write it to the file
                file?.write(jsonData)
                
            } catch  {    }
            file?.closeFile()
        } else {
            print("Ooops! Something went wrong!  file is nil ")
        }
        
    }
    
    
    func displayButtons(_ nombre:Int) {
        var i:Int = 0
        let segment = difficultySegment.selectedSegmentIndex
        let difficulty = ldefine.allLevel[segment]

        for button in arrayLevelButtons {
            if (i<nombre) {
                button.isHidden = false
                var penta:APenta? = nil
                for anyPenta in pentas {
                    if anyPenta.name == arrayLevels[difficulty]![button.tag] {
                        penta = anyPenta
                        break
                    }
                }
                if penta?.data?[0].count != penta?.width {
                    penta?.width = penta?.data?[0].count
                    print ("******************* Erroneous width in Json for \(String(describing: penta?.name))- fixing it ")
                }
                if penta?.data?.count != penta?.height {
                    penta?.height = penta?.data?.count
                    print ("******************* Erroneous height in Json for \(String(describing: penta?.name))- fixing it ")
                }
                drawMiniPenta(button: button, penta: penta!)

            } else {
                button.isHidden = true
            }
            i=i+1
        }
    }
  
    @IBAction func buttonAction(_ sender: UIButton) {
        let segment = difficultySegment.selectedSegmentIndex
        let difficulty = ldefine.allLevel[segment]
        var currentLevel:Int = sender.tag
        
        let listLevels:[String] = arrayLevels[difficulty]!
        let maxLevel = listLevels.count
        
        if currentLevel >= maxLevel {
            currentLevel = maxLevel - 1
        }
        labelCurrentLevel.text = arrayLevels[difficulty]![currentLevel]

        var penta:APenta? = nil
        for anyPenta in pentas {
            if anyPenta.name == arrayLevels[difficulty]![currentLevel] {
                penta = anyPenta
                break
            }
        }
        let next:playViewController = playViewController()
        next.setPenta(penta!)
        var index = 0
        var found:Bool = false
        for backup in globalUserGameData.totale! {
            if backup.name == penta?.name {
                found = true
                break
            }
            index = index + 1

        }
        if found { next.setVSet(vset:globalUserGameData.totale![index].vSet!, index:index) }

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
    
    func get_neighbour(penta:APenta, _ i:Int, _ j:Int) -> neighbour {
        let maxj=penta.data!.count-1
        let maxi=penta.data![i].count-1
        var myNeighbour:neighbour = neighbour.init(up: false, down: false, right: false, left: false)
        let val = penta.data![i][j]
        
        if j == 0 { myNeighbour.left = false }
        else { myNeighbour.left = penta.data![i][j-1] == val }
        
        if i == 0 { myNeighbour.up = false }
        else { myNeighbour.up = penta.data![i-1][j] == val }
        
        if j == maxi { myNeighbour.right = false }
        else { myNeighbour.right = penta.data![i][j+1] == val }
        
        if i == maxj { myNeighbour.down = false }
        else { myNeighbour.down = penta.data![i+1][j] == val }
        
        return myNeighbour
    }
    
    func drawRect(_ ctx:CGContext, _ x:CGFloat,_ y:CGFloat,_ width:CGFloat,_ height:CGFloat,_ up:Bool,_ down:Bool,_ right:Bool,_ left:Bool) {
        let color = UIColor.black.cgColor
        
        drawLine(ctx,x,y,x+width,y,up,color)
        drawLine(ctx,x,y+height,x+width,y+height,down,color)
        drawLine(ctx,x,y,x,y+height,left,color)
        drawLine(ctx,x+width,y,x+width,y+height,right,color)
    }
    
    func drawLine(_ context:CGContext,_ x:CGFloat,_ y:CGFloat, _ xdest:CGFloat, _ ydest:CGFloat, _ dash:Bool, _ color:CGColor ) {
        let startPoint = CGPoint(x: x, y: y)
        let endPoint = CGPoint(x: xdest, y: ydest)
        context.move(to: startPoint)
        context.addLine(to: endPoint)
        if dash {
            context.setLineWidth(1)
            context.setLineDash(phase: 0, lengths: [4, 4])
        } else {
            context.setLineWidth(2)
            context.setLineDash(phase: 0, lengths: [])
        }
        context.setStrokeColor(color)
        context.strokePath()
    }
    

    func drawMiniPenta(button:UIButton, penta:APenta) {
        let width:Int = penta.width!
        let height:Int = penta.height!
        let myImageView:UIImageView = button.imageView!
        var sizeSquare : CGFloat
        var dwidth:CGFloat = CGFloat(0)
        var dheight:CGFloat = CGFloat(0)
        
        var h = myImageView.bounds.size.height
        var w = myImageView.bounds.size.width
        
        h = butHeightO
        w = butWidthO
        
        
        if h < w {
            sizeSquare = h-5
        } else {
            sizeSquare = w-5
        }
        let divSize = CGFloat(max(width,height))
        let sizeBut = sizeSquare / divSize
        
        let sizeFont = CGFloat(sizeBut) * 0.8
        if width < height {
            dwidth = CGFloat(height - width) * sizeBut / 2
        } else {
            dheight = CGFloat(width - height) * sizeBut / 2
        }
        
        let renderer = UIGraphicsImageRenderer(size: CGSize(width: sizeSquare, height: sizeSquare))
        
        let img = renderer.image { ctx in
            
            let color = UIColor(rgb: 0xcccccc)
            
            ctx.cgContext.setFillColor(color.cgColor)
            ctx.cgContext.fill(CGRect(x: 0, y: 0, width: sizeSquare, height: sizeSquare))
            
            for i in 0..<height {
                for j in 0..<width {
                    let x=sizeBut/6+CGFloat(j)*sizeBut
                    let y=CGFloat(i)*sizeBut
                    let itsNeighbour = get_neighbour(penta:penta,i,j)
                    
                    drawRect(ctx.cgContext, CGFloat(x)+dwidth, CGFloat(y)+dheight, CGFloat(sizeBut), CGFloat(sizeBut), itsNeighbour.up, itsNeighbour.down, itsNeighbour.right, itsNeighbour.left)
                    
                }
            }
            let atts = [NSAttributedStringKey.font: UIFont.systemFont(ofSize: sizeFont),NSAttributedStringKey.foregroundColor:UIColor.black]
            
            for arrayValeur in penta.values! {
                
                let x = dwidth + sizeBut*0.5 + CGFloat(arrayValeur.j!)*sizeBut
                let y = dheight + CGFloat(arrayValeur.i!)*sizeBut
                
                ("\(arrayValeur.val!)" as NSString).draw(at: CGPoint(x: x, y: y), withAttributes: atts)
            }
            
            //TBD
            var complete:Bool = false
            for backup in globalUserGameData.totale! {
                if backup.name == penta.name {
                    if backup.completed == nil {
                        complete = false
                    } else {
                    complete = backup.completed!
                    }
                    break
                }
            }
            if complete {
                var sizeStr = "100"
                if sizeBut*max(CGFloat(penta.width!),CGFloat(penta.height!)) < 100 { sizeStr = "40" }
                let image = UIImage(named: "completed"+sizeStr+".png")!
                ctx.cgContext.draw(image.cgImage!, in: CGRect(x: 0.0,y: 0.0,width: image.size.width,height: image.size.height))
            }

        }
        button.setImage(img, for: UIControlState.normal)

        let longPress = UILongPressGestureRecognizer(target: self, action: #selector(longPressAction(_:)))
        longPress.minimumPressDuration = 1.5;
        button.addGestureRecognizer(longPress)

        //image.image = img
    }
    

    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */
    @objc func respondToSwipeGesture(gesture: UIGestureRecognizer) {
        if let swipeGesture = gesture as? UISwipeGestureRecognizer {
            switch swipeGesture.direction {
            case UISwipeGestureRecognizerDirection.right:
                var segment = difficultySegment.selectedSegmentIndex
                if segment > 0 {
                    segment = segment-1
                    difficultySegment.selectedSegmentIndex = segment
                    let currentDifficulty = ldefine.allLevel[segment]
                    let currentMaxLevel = (arrayLevels[currentDifficulty]?.count)!
                    displayButtons(currentMaxLevel)
                    labelCurrentLevel.text = ""
                    
                }

            case UISwipeGestureRecognizerDirection.down:
                print("Swiped down")
            case UISwipeGestureRecognizerDirection.left:
                var segment = difficultySegment.selectedSegmentIndex
                if segment < ldefine.allLevel.count-1 {
                    segment = segment+1
                    difficultySegment.selectedSegmentIndex = segment
                    let currentDifficulty = ldefine.allLevel[segment]
                    let currentMaxLevel = (arrayLevels[currentDifficulty]?.count)!
                    displayButtons(currentMaxLevel)
                    labelCurrentLevel.text = ""
                    
                }

            case UISwipeGestureRecognizerDirection.up:
                print("Swiped up")
            default:
                break
            }
            
        }
    }

    @objc func longPressAction(_ sender: UIGestureRecognizer) {
        
        if sender.state == .ended {
            print("Long press Ended")
        } else if sender.state == .began {
            print("Long press detected")
            var currentLevel:Int = (sender.view?.tag)!
            let segment = difficultySegment.selectedSegmentIndex
            let difficulty = ldefine.allLevel[segment]
            let maxLevel = arrayLevels[difficulty]!.count

            if currentLevel >= maxLevel {
                currentLevel = maxLevel - 1
            }

            labelCurrentLevel.text = arrayLevels[difficulty]![currentLevel]

            
        }
    }


}

