//
//  playViewController.swift
//  Pentatonic
//
//  Created by Armand Hornik on 19/02/2018.
//  Copyright © 2018 Armand Hornik. All rights reserved.
//

import UIKit
struct neighbour {
    var up:Bool
    var down:Bool
    var right:Bool
    var left:Bool
}
struct IJ {
    var i:Int
    var j:Int
    var val:String
    init (p:APenta,val:Int) {
        i = val / p.width!
        j = val % p.width!
        self.val = p.data![i][j]
    }
    func getIndex(_ p:APenta, _ i:Int,_ j:Int) -> Int {
        let index = i * p.width! + j
        return index
    }
}
class playViewController: UIViewController {
    var arrayPlayButtons:[UIButton] = []
    var initialized:Bool = false
    var currentPenta:APenta? = nil
    var screenWidth:CGFloat = 0
    var screenHeight:CGFloat = 0
    var minW:Int = 0
    var area:[[Int]] = [[]]
    var valueSet:[Int] = []
    var selectedValue:Int = -1
    @IBOutlet var myviewImage: UIImageView!
    
    public func setPenta (_ penta:APenta) {
        currentPenta = penta
        let width = penta.width
        let height = penta.height
        let size:Int = width! * height!
        area = Array(repeating: [], count: size)
        print ("width=\(width!) height=\(height!)")
        valueSet=Array(repeating:0, count: size)
        
        for allVal in 0..<size {
            let lIJ = IJ(p: currentPenta!, val: allVal)
            let i=lIJ.i
            let j=lIJ.j
            if i != 0 {
                area[allVal].append(lIJ.getIndex(penta,i-1,j))
                if j != 0 {
                    area[allVal].append(lIJ.getIndex(penta, i-1, j-1))
                }
                if j != (width!-1) {
                    area[allVal].append(lIJ.getIndex(penta, i-1, j+1))
                }
            }
            if i != (height!-1) {
                area[allVal].append(lIJ.getIndex(penta,i+1,j))
                
            }
            
            if j != 0 {
                area[allVal].append(lIJ.getIndex(penta, i, j-1))
                if i != (height!-1) {
                    area[allVal].append(lIJ.getIndex(penta, i+1, j-1))
                }
            }
            if j != (width!-1)
            {
                area[allVal].append(lIJ.getIndex(penta, i, j+1))
                if i != (height!-1) {
                    area[allVal].append(lIJ.getIndex(penta, i+1, j+1))
                }
            }

            print ("area <=> \(allVal) = data[\(lIJ.i)][\(lIJ.j)] = \(penta.data![lIJ.i][lIJ.j])")
            for allVal2 in 0..<size {
                let dIJ = IJ(p:currentPenta!, val:allVal2)
                if lIJ.val == dIJ.val { area[allVal].append(allVal2)}
            }
            
        }
        for i in 0..<area.count {
            area[i] = Array(Set(area[i]))
            area[i].sort()
        }
        for i in 0..<area.count {
            print ("\(i) is incompatible with \(area[i]) ")
        }
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        let screenSize: CGRect = UIScreen.main.bounds
        screenWidth = screenSize.width
        screenHeight = screenSize.height
        minW = min(Int(screenHeight),Int(screenWidth))
        
        myviewImage = UIImageView(frame:CGRect(x: 0, y: 0, width: minW, height: minW))
        self.view.addSubview(myviewImage)
        
        if !initialized {
            showTheGrid((currentPenta?.width)!, (currentPenta?.height)!)
            initialized = true
        }
        drawPenta(penta: currentPenta!, valSelected: -1, darker: [])
        addBackButton()
    }
    
    func showTheGrid(_ width:Int, _ height:Int) {
        var sizeBut:Int
        var initialX:Int
        var initialY:Int
        let NBButton = max(width,height)
        
        let divSize:Int = NBButton + 2
        sizeBut = minW / divSize
        initialX = sizeBut
        initialY = sizeBut*2
        var tag=0
        for j in 0..<height {
            for i in 0..<width {
                arrayPlayButtons.append(addButton(x: initialX+i*sizeBut, y: initialY+j*sizeBut, size: sizeBut, color: .clear, tag: tag))
                tag=tag+1
            }
        }
    }
    
    func addButton (x:Int, y:Int, size:Int, color: UIColor, tag:Int) -> UIButton {
        let myImage:UIImage = UIImage(named: "transparent.png")!
        let button = UIButton(type: UIButtonType.custom)
        let rect = CGRect(x:x,y:y,width:size,height:size)
        button.frame = rect
        button.setImage(myImage, for: UIControlState.normal)
        button.backgroundColor = color
        button.addTarget(self, action: #selector(buttonAction), for: .touchUpInside)
        button.tag = tag
        self.view.addSubview(button)
        return button
    }
    func addBackButton() {
        
        
        let margins = self.view.layoutMarginsGuide
        
        let backButton = UIButton()
        self.view.addSubview(backButton)
        
        backButton.setTitle("Back", for: [])
        backButton.backgroundColor = UIColor.white
        backButton.setTitleColor(UIColor.blue, for: UIControlState.normal)
        backButton.layer.cornerRadius = 3
        backButton.sizeToFit()
        backButton.translatesAutoresizingMaskIntoConstraints = false
        
        backButton.bottomAnchor.constraint(equalTo: margins.bottomAnchor, constant: -20).isActive = true
        //backButton.leadingAnchor.constraint(equalTo: margins.leadingAnchor).isActive = true
        backButton.trailingAnchor.constraint(equalTo: margins.trailingAnchor, constant: -20).isActive = true
        backButton.tag = 999
        backButton.addTarget(self, action: #selector(buttonAction), for: .touchUpInside)

    }
        

        
        
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func drawPenta(penta:APenta, valSelected:Int, darker:[Int] ) {
        let width:Int = penta.width!
        let height:Int = penta.height!
        var widthBut:Int
        var heightBut:Int
        var initialX:Int
        var initialY:Int
        let NBButton = max(width,height)
        let divSize:Int = NBButton + 2
        widthBut = minW / divSize
        heightBut = widthBut
        initialX = widthBut
        initialY = heightBut*2
        let maxI = initialX/4+height*widthBut
        let maxJ = initialY/2+width*heightBut + heightBut
        if (Int(screenWidth) - maxI) > (Int(screenHeight) - maxJ) {
            print ("USING I : \((screenWidth - CGFloat(maxI))/5.0)  and not  \((screenHeight - CGFloat(maxJ))/5.0)")
        } else {
            print ("USING J \((screenHeight - CGFloat(maxJ))/5.0)  and not  \((screenWidth - CGFloat(maxI))/5.0) ")
            print ("from ")
        }
       
        
        let renderer = UIGraphicsImageRenderer(size: CGSize(width: minW, height: minW))
        
        let img = renderer.image { ctx in
            let listColor = [ldefine.currentTheme[define.LColor.dark]?.cgColor,
                             ldefine.currentTheme[define.LColor.light]?.cgColor,
                             ldefine.currentTheme[define.LColor.lighter]?.cgColor]
            var color:Int
            let lIJ:IJ = IJ(p: penta, val: 0)
            let atts = [NSAttributedStringKey.font: UIFont.systemFont(ofSize: CGFloat(heightBut) * 0.8)]
            for i in 0..<height {
                for j in 0..<width {
                    let val:Int = lIJ.getIndex(penta,i,j)
                    if darker.contains(val) { color = 1} else { color = 2}
                    if val == valSelected { color = 0}

                    ctx.cgContext.setFillColor(listColor[color]!)
                    let x=initialX+j*widthBut
                    let y=initialY+i*heightBut
                    ctx.cgContext.fill(CGRect(x: x, y: y, width: widthBut, height: heightBut))
                    let itsNeighbour = get_neighbour(penta:penta,j,i)
                    
                    drawRect(ctx.cgContext, CGFloat(x), CGFloat(y), CGFloat(widthBut), CGFloat(heightBut), itsNeighbour.up, itsNeighbour.down, itsNeighbour.right, itsNeighbour.left)
                    
                }
                for valeur in penta.valeurs! {
                    let val:Int = valeur.val!
                    let i:Int = valeur.j!+1
                    let j:Int = valeur.i!+1
                    
                    ("\(val)" as NSString).draw(
                        at: CGPoint(x: initialX/4+i*widthBut, y: initialY/2+j*heightBut),
                        withAttributes: atts)

                }

            }
        }
        
        myviewImage.image = img
    }
    func get_neighbour(penta:APenta, _ i:Int, _ j:Int) -> neighbour {
        let maxj=penta.data!.count-1
        let maxi=penta.data![j].count-1
        var myNeighbour:neighbour = neighbour.init(up: false, down: false, right: false, left: false)
        let val = penta.data![j][i]

        if i == 0 { myNeighbour.left = false }
        else { myNeighbour.left = penta.data![j][i-1] == val }
        
        if j == 0 { myNeighbour.up = false }
        else { myNeighbour.up = penta.data![j-1][i] == val }
        
        if i == maxi { myNeighbour.right = false }
        else { myNeighbour.right = penta.data![j][i+1] == val }
        
        if j == maxj { myNeighbour.down = false }
        else { myNeighbour.down = penta.data![j+1][i] == val }
        
        return myNeighbour
    }
    
    func drawRect(_ ctx:CGContext, _ x:CGFloat,_ y:CGFloat,_ width:CGFloat,_ height:CGFloat,_ up:Bool,_ down:Bool,_ right:Bool,_ left:Bool) {
        drawLine(ctx,x,y,x+width,y,up)
        drawLine(ctx,x,y+height,x+width,y+height,down)
        drawLine(ctx,x,y,x,y+height,left)
        drawLine(ctx,x+width,y,x+width,y+height,right)
    }
    
    func drawLine(_ context:CGContext,_ x:CGFloat,_ y:CGFloat, _ xdest:CGFloat, _ ydest:CGFloat, _ dash:Bool) {
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
        context.setStrokeColor(UIColor.black.cgColor)
        context.strokePath()
    }
  
    @IBAction func buttonAction(_ sender: UIButton) {
        var noArea:[Int] = []
        
        var currentLevel:Int = sender.tag
        if currentLevel == 999 {
            // We did press Back button

            dismiss(animated: true, completion: nil)
            return
        }
        if selectedValue != currentLevel {
            selectedValue = currentLevel
            noArea = area[currentLevel]
        } else {
            selectedValue = -1
            currentLevel = -1
            noArea = []
        }
        drawPenta(penta: currentPenta!, valSelected: currentLevel, darker: noArea)
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
extension UIColor {
    convenience init(rgb: UInt) {
        self.init(
            red: CGFloat((rgb & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgb & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgb & 0x0000FF) / 255.0,
            alpha: CGFloat(1.0)
        )
    }
}
