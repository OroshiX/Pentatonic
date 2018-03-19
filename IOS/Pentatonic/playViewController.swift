//
//  playViewController.swift
//  Pentatonic
//
//  Created by Armand Hornik on 19/02/2018.
//  Copyright © 2018 Armand Hornik. All rights reserved.
//

import UIKit

//MARK: Some Classes used by the playViewController
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
    init (p:APenta,index:Int) {
        i = index / p.width!
        j = index % p.width!
        self.val = p.data![i][j]
    }
    init (p:APenta, i:Int, j:Int) {
        self.i=i
        self.j=j
        self.val=String("\(p.data![i][j])")
    }
    func getIndex(_ p:APenta, _ i:Int,_ j:Int) -> Int {
        let index = i * p.width! + j
        return index
    }
}
struct action {
    var cell:Int
    var value:Int
}
class playViewController: UIViewController ,UIPickerViewDelegate, UIPickerViewDataSource{
    //MARK: The variables used by the class
    var arrayPlayButtons:[UIButton] = []
    var initialized:Bool = false
    var currentPenta:APenta? = nil
    var initialX:Int = 0
    var initialY:Int = 0
    var sizeBut:Int = 0
    var sizeFont:CGFloat = 0
    var petiteFont:CGFloat = 0
    
    var dxl:CGFloat = 0
    var dxm:CGFloat = 0
    var dxr:CGFloat = 0
    var dyu:CGFloat = 0
    var dym:CGFloat = 0
    var dyd:CGFloat = 0
    
    var currentPentaSolved:Bool = false
    var globalIndex = -1
    
    var screenWidth:CGFloat = 0
    var screenHeight:CGFloat = 0
    var minW:Int = 0
    var sameAreaCells:[Set<Int>] = [[]]
    // vSet will contain the origina values (negative)
    // and set of value user want to pre-select
    
    var vSet :[Set<Int>] = [[]]
    var regionCount :[Int] = []
    var possibleValue : [Set<Int>] = [[]]
    var selectedValue:Int = -1
    var whatTag = ["a","b","c","d","e","x","y","z","v","w"]
    
    
    var pastAction:[action] = []
    var futurAction:[action] = []
    
    @IBOutlet var myBackgroundImage: UIImageView!
    @IBOutlet var myviewImage: UIImageView!
    @IBOutlet var myNumbersImage: UIImageView!
    @IBOutlet var labelTitlePenta:UIButton!
    @IBOutlet var labelSolved:UIButton!

    //MARK: The functions
    /// This function set default value for the controller to be able to display the Penta
    /// This function must be called before any invocation of this controller.
    ///
    /// - Parameter penta: This is the next penta to be display and played in this controller
    ///
    
    public func setPenta (_ penta:APenta) {
        currentPenta = penta
        
        let screenSize: CGRect = UIScreen.main.bounds
        screenWidth = screenSize.width
        screenHeight = screenSize.height
        
        let width = penta.width
        let height = penta.height
        

        let size:Int = width! * height!
        
        // For the buttons
        minW = min(Int(screenHeight),Int(screenWidth))
        if minW == 0 { return }
        let NBButton = max(width!,height!)
        let divSize:Int = NBButton + 2
        sizeBut = minW / divSize
        initialX = sizeBut
        initialY = sizeBut*2
        sizeFont = CGFloat(sizeBut) * 0.8
        //*************************
        
        sizeFont = CGFloat(sizeBut) * 0.8
        
        petiteFont = CGFloat(sizeBut) * 0.8 / 3
        
        dxm = 0-CGFloat(sizeBut) / 4.8
        dxl = dxm + CGFloat(sizeBut) / 2.5
        dxr = dxm - CGFloat(sizeBut) / 2.7
        
        dyd = CGFloat(sizeBut) / 1.5
        dym = CGFloat(sizeBut) / 3.3
        dyu = CGFloat(0.0)
        
        
        
        regionCount = Array(repeating:0,count:size)
        sameAreaCells = Array(repeating:Set<Int>(), count:size)
        possibleValue = Array(repeating:Set<Int>(),count:size)
        
        vSet = Array(repeating:Set<Int>(),count:size)
        
        var arrayRegionSet : [String:Set<Int>] = [:]
        
        for allVal in 0..<size {
            
            let lIJ = IJ(p: currentPenta!, index: allVal)
            let i=lIJ.i
            let j=lIJ.j
            if (arrayRegionSet[lIJ.val]?.count == nil ) {
                arrayRegionSet[lIJ.val] = []
            }
            arrayRegionSet[lIJ.val]?.insert(allVal)
            
            if i != 0 {
                sameAreaCells[allVal].insert(lIJ.getIndex(penta, i-1, j))
                if j != 0 {
                    sameAreaCells[allVal].insert(lIJ.getIndex(penta, i-1, j-1))
                }
                if j != (width!-1) {
                    sameAreaCells[allVal].insert(lIJ.getIndex(penta, i-1, j+1))
                }
            }
            if i != (height!-1) {
                sameAreaCells[allVal].insert(lIJ.getIndex(penta,i+1,j))
                
            }
            
            if j != 0 {
                sameAreaCells[allVal].insert(lIJ.getIndex(penta, i, j-1))
                if i != (height!-1) {
                    sameAreaCells[allVal].insert(lIJ.getIndex(penta, i+1, j-1))
                }
            }
            if j != (width!-1)
            {
                sameAreaCells[allVal].insert(lIJ.getIndex(penta, i, j+1))
                if i != (height!-1) {
                    sameAreaCells[allVal].insert(lIJ.getIndex(penta, i+1, j+1))
                }
            }
            
            for allVal2 in 0..<size {
                let dIJ = IJ(p:currentPenta!, index:allVal2)
                if lIJ.val == dIJ.val { sameAreaCells[allVal].insert(allVal2)}
            }
            
        }
        for regionSet in arrayRegionSet {
            
            for cell in regionSet.value {
                regionCount[cell] = regionSet.value.count
                for i in 1...regionCount[cell] {
                    possibleValue[cell].insert(i)
                }
            }
        }
        
        for valeur in (currentPenta?.values)! {
            let val:Int = valeur.val!
            let lIJ:IJ = IJ.init(p: currentPenta!, index: 0)
            let i:Int = valeur.i!
            let j:Int = valeur.j!
            let index = lIJ.getIndex(currentPenta!, i, j)
            vSet[index] = [ -val ]
            possibleValue[index] = [val]
            for i in sameAreaCells[index] {
                if i != index { possibleValue[i].remove(val) }
            }
        }
        if ldefine.helpButtonValue {
            var change=true
            while change {
                change=false
                for i in 0..<size {
                    if possibleValue[i].count == 1 && vSet[i].count == 0 {
                        vSet[i]=[possibleValue[i].first!]
                        change = true
                        for j in sameAreaCells[i] {
                            if i != j {
                                possibleValue[j].remove(possibleValue[i].first!)
                                
                            }
                        }
                        
                    }
                }
                for regionSet in arrayRegionSet {
                    var local:Set<Int> = []
                    for pentaVal in 1...regionSet.value.count {
                        // Local is the list of cells which can contain pentaVal
                        // if local.count is 1 then only one cell in this region can contain this value
                        local = []
                        
                        for cell in regionSet.value {
                            
                            if possibleValue[cell].contains(pentaVal) {
                                local.insert(cell)
                            }
                        }
                        if local.count == 1 && possibleValue[local.first!].count != 1 {
                            possibleValue[local.first!] = [ pentaVal]
                            vSet[local.first!] = [pentaVal]
                            for j in sameAreaCells[local.first!] {
                                if local.first! != j {
                                    possibleValue[j].remove(pentaVal)
                                    
                                }
                                
                            }
                            
                            change = true
                        }
                    }
                }
                if change == false {
                    for regionSet in arrayRegionSet {
                        
                        var neighbourWholeRegion:Set<Int>? = nil
                        
                        for cell in regionSet.value {
                            let A = sameAreaCells[cell]
                            
                            if neighbourWholeRegion == nil {
                                neighbourWholeRegion = A.subtracting(regionSet.value)
                            } else {
                                neighbourWholeRegion = neighbourWholeRegion?.intersection(A.subtracting(regionSet.value))
                            }
                        }
                        for pentaVal in 1...regionSet.value.count {
                            for cell in neighbourWholeRegion! {
                                if possibleValue[cell].contains(pentaVal) {
                                   possibleValue[cell].remove(pentaVal)
                                    change = true
                                }
                            }
                        }
                        print ("region:\(regionSet.key): all cellse have \(String(describing: neighbourWholeRegion)) as neighbourssubtracting")
                    }
                }
                
            }
            
            
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        let image = UIImage(named: "background2.png")!
        myBackgroundImage = UIImageView(image: image)
        
        self.view.addSubview(myBackgroundImage)
        
        let trailing = self.view.layoutMarginsGuide.trailingAnchor
        let leading = self.view.layoutMarginsGuide.leadingAnchor
        let top = self.view.layoutMarginsGuide.topAnchor
        let bottom = self.view.layoutMarginsGuide.bottomAnchor

        myBackgroundImage.translatesAutoresizingMaskIntoConstraints = false
        
        myBackgroundImage.trailingAnchor.constraint(equalTo: trailing, constant: 0).isActive = true
        myBackgroundImage.leadingAnchor.constraint(equalTo: leading, constant: 0).isActive = true
        myBackgroundImage.topAnchor.constraint(equalTo: top, constant: 0).isActive = true
        myBackgroundImage.bottomAnchor.constraint(equalTo: bottom, constant: 0).isActive = true
        
        
        
        
        myviewImage = UIImageView(frame:CGRect(x: 0, y: 0, width: minW, height: minW))
        self.view.addSubview(myviewImage)
        

        
        myNumbersImage = UIImageView(frame:CGRect(x:0,y:minW,width:minW,height:(Int(screenHeight) - minW)))
        self.view.addSubview(myNumbersImage)
        
        //***********
        let renderer = UIGraphicsImageRenderer(size: CGSize(width: Int(minW), height: (Int(screenHeight) - minW)))
        let img = renderer.image { ctx in
            
            ctx.cgContext.setFillColor(UIColor.gray.cgColor)
            let widthAreaBut = CGFloat(((currentPenta?.width)!+1)*sizeBut)
            let heightAreaBut = screenHeight - CGFloat(minW) - 10.0
            let xmin = CGFloat(sizeBut)
            
            drawNumbersButton(ctx: ctx.cgContext, xmin: xmin, ymin: 0, xmax: widthAreaBut, ymax: heightAreaBut)
            //drawLine(ctx.cgContext, xmin, 0, widthAreaBut, heightAreaBut , false, UIColor.red.cgColor)
        }
        myNumbersImage.image = img
        //**********
        
        
        
        if !initialized {
            showTheGrid((currentPenta?.width)!, (currentPenta?.height)!)
            initialized = true
        }
        drawPenta(penta: currentPenta!, valSelected: -1, darker: [])
        addBackButton()
        
    }
    
    func showTheGrid(_ width:Int, _ height:Int) {
        
        var tag=0
        for j in 0..<height {
            for i in 0..<width {
                arrayPlayButtons.append(addButton(x: initialX+i*sizeBut, y: initialY+j*sizeBut, size: sizeBut, color: .clear, tag: tag))
                tag=tag+1
            }
        }
    }
    
    
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func drawNumbersButton (ctx:CGContext,xmin:CGFloat, ymin:CGFloat,xmax:CGFloat, ymax:CGFloat) {
        let nbNButtonsX = 5
        let nbNButtonsY = 3
        let interNButton = 5
        
        /*
         *     B1 B2 B3 B4 B5
         *     Ba Bb Bc Bd Be
         *     Bx By Bz Bv Bw
         *
         *   Buttons have "interButton" distance
         */
        //ctx.fill(CGRect(x: xmin, y:ymin, width: xmax-xmin, height: ymax-ymin))
        let widthNBut = (Int(xmax - xmin) / nbNButtonsX) - interNButton
        let heightNBut = (Int(ymax - ymin) / nbNButtonsY) - (nbNButtonsY-1)*interNButton
        let atts = [NSAttributedStringKey.font:
            UIFont.systemFont(ofSize: min(CGFloat(widthNBut),CGFloat(heightNBut))*0.8),
                    NSAttributedStringKey.foregroundColor:UIColor.black]
        
        let cote = min (widthNBut,heightNBut)
        for i in 0..<nbNButtonsX {
            for j in 0..<nbNButtonsY {
                let x = xmin+CGFloat((cote+interNButton)*i)
                let y = ymin+CGFloat((cote+interNButton)*j)
                let w = CGFloat(cote)
                let h = CGFloat(cote)
                let tag = j*nbNButtonsX+i+1
                ctx.setFillColor(UIColor.gray.cgColor)
                
                ctx.fill(CGRect(x:x, y:y, width:w,height:h ))
                addNButton(x:x, y:y, width:w, height:h, color:UIColor.clear, tag:tag)
                
                (getStringNButton(tag) as NSString).draw(at: CGPoint(x: x+w/4, y: y), withAttributes: atts)
                
            }
        }
    }
    func getStringNButton(_ val:Int) -> String{
        var ret:String
        
        switch val {
        case 1...5:
            ret="\(val)"
            
        case 6..<whatTag.count+6:
            ret=whatTag[val-6]
            
        default:
            ret="?"
        }
        return ret
    }

    
    
    
    
    
    func drawPenta(penta:APenta, valSelected:Int, darker:Set<Int> ) {
        let width:Int = penta.width!
        let height:Int = penta.height!
        
        let maxI = initialX/4+height*sizeBut
        let maxJ = initialY/2+width*sizeBut + sizeBut
        if (Int(screenWidth) - maxI) > (Int(screenHeight) - maxJ) {
            //print ("USING I : \((screenWidth - CGFloat(maxI))/5.0)  and not  \((screenHeight - CGFloat(maxJ))/5.0)")
        } else {
            //print ("USING J \((screenHeight - CGFloat(maxJ))/5.0)  and not  \((screenWidth - CGFloat(maxI))/5.0) ")
            //print ("from ")
        }
        var index:Int = 0
        var listSist:[String] = Array(repeating:"" , count:width * height)
        let listSymbol:[String] = [ "-", "~", "±", "§", "^", "#"]
        for sister in penta.sisters! {
            var symbol = sister.symbol!
            symbol = listSymbol[index]
            index = (index+1)%listSymbol.count
            // since symbol is not yet correctly initialized in json, lets use our OWN
            // TBD
            for position in sister.positions! {
                listSist[position.i!*width + position.j!] = symbol
            }
        }
        let renderer = UIGraphicsImageRenderer(size: CGSize(width: minW, height: minW))
        var nbGoodColor = 0
        let img = renderer.image { ctx in
            let listColor = [ldefine.currentTheme[define.LColor.dark]?.cgColor,
                             ldefine.currentTheme[define.LColor.light]?.cgColor,
                             ldefine.currentTheme[define.LColor.lighter]?.cgColor]
            var color:Int
            let lIJ:IJ = IJ(p: penta, index: 0)
            for i in 0..<height {
                for j in 0..<width {
                    let val:Int = lIJ.getIndex(penta,i,j)
                    if darker.contains(val) { color = 1} else { color = 2}
                    if val == valSelected { color = 0}
                    
                    ctx.cgContext.setFillColor(listColor[color]!)
                    let x=initialX+j*sizeBut
                    let y=initialY+i*sizeBut
                    ctx.cgContext.fill(CGRect(x: x, y: y, width: sizeBut, height: sizeBut))
                    let itsNeighbour = get_neighbour(penta:penta,i,j)
                    // print ("i=\(i),j=\(j) - val=\(penta.data![i][j]) - x=\(x),y=\(y)")
                    drawRect(ctx.cgContext, CGFloat(x), CGFloat(y), CGFloat(sizeBut), CGFloat(sizeBut), itsNeighbour.up, itsNeighbour.down, itsNeighbour.right, itsNeighbour.left)
                    
                }
            }
            //print("will use \(sizeFont) for font size - initialX \(initialX) initialY\(initialY) width \(sizeBut)")
            var atts = [NSAttributedStringKey.font: UIFont.systemFont(ofSize: sizeFont),NSAttributedStringKey.foregroundColor:UIColor.black]
            var index:Int = -1
            let petiteListeXY:[[CGFloat]] = [ [dxl,dyu],[dxr,dyu],[dxl,dyd],[dxr,dym],[dxm,dym],[dxm,dyu],[dxm,dyd],[dxl,dym],[dxr,dyd]]
            for arrayValeur in vSet {
                index = index+1
                let lIJ = IJ(p: currentPenta!, index: index)
                
                if arrayValeur.count == 1 {
                    let x = (initialX/4+(lIJ.j+1)*sizeBut)
                    let y = (initialY/2+(lIJ.i+1)*sizeBut)
                    var valeur = arrayValeur.first!
                    
                    if (valeur < 0) {
                        // black
                        atts = [NSAttributedStringKey.font: UIFont.systemFont(ofSize: sizeFont),                  NSAttributedStringKey.foregroundColor: UIColor.black]
                        valeur = -valeur
                        nbGoodColor = nbGoodColor + 1
                    } else {
                        // blue or red
                        var col:UIColor = UIColor.blue
                        if vSet[index].count == 1 && vSet[index].first! > regionCount[index]  && vSet[index].first! < 6 {
                            col = UIColor.red
                        } else {
                        for cellIndex in sameAreaCells[index] {
                            
                            if cellIndex != index && vSet[cellIndex].count == 1 && abs(vSet[cellIndex].first!) == abs(valeur) {
                                col = UIColor.red
                            }
                            
                        }
                        }
                        if col == UIColor.blue && vSet[index].first! < 6{
                            nbGoodColor = nbGoodColor + 1
                        }
                        atts = [NSAttributedStringKey.font: UIFont.systemFont(ofSize: sizeFont),                  NSAttributedStringKey.foregroundColor: col]
                        
                    }
                    (getStringNButton(valeur) as NSString).draw(at: CGPoint(x: x, y: y), withAttributes: atts)
                    
                } else {
                    atts = [NSAttributedStringKey.font: UIFont.systemFont(ofSize: petiteFont)]
                    
                    var coord:Int = -1
                    for valeur in arrayValeur {
                        coord = coord+1
                        if coord >=  petiteListeXY.count-1 {
                            break
                        }
                        let x = (initialX/4+(lIJ.j+1)*sizeBut) - Int(petiteListeXY[coord][0])
                        let y = (initialY/2+(lIJ.i+1)*sizeBut) + Int(petiteListeXY[coord][1])
                        (getStringNButton(valeur) as NSString).draw(at: CGPoint(x: x, y: y), withAttributes: atts)
                        
                        
                    }
                }
                if listSist[index] != "" {
                    let x = (initialX/4+(lIJ.j+1)*sizeBut) - Int(petiteListeXY.last![0]*0.9)
                    let y = (initialY/2+(lIJ.i+1)*sizeBut) + Int(petiteListeXY.last![1]*0.9)
                    atts = [NSAttributedStringKey.font: UIFont.systemFont(ofSize: petiteFont*1.4)]

                    listSist[index].draw(at: CGPoint(x: x, y: y), withAttributes: atts)
                }

            }
            var pos:Int = 0
            
            for difference in penta.differences! {
                pos = 0
                let i1:Int = (difference.position1?.i)!
                let j1:Int = (difference.position1?.j)!
                let i2:Int = (difference.position2?.i)!
                let j2:Int = (difference.position2?.j)!
                if i1 ==  i2 {
                    pos = 10
                } else if i1 < i2 {
                    pos = 20
                }
                if j1 == j2 {
                    pos = pos + 1
                } else if j1 < j2 {
                    pos = pos + 2
                }
                let c:CGFloat = CGFloat(sizeBut)
                var x:CGFloat = CGFloat(initialX+j1*sizeBut)
                var y:CGFloat = CGFloat(initialY+i1*sizeBut)

                /*
                 *      00--01--02
                 *       |      |
                 *      10  pos 12
                 *       |      |
                 *      20--21--22
                 *
                 */
                
                switch pos {
                case 0:
                    x = x - c/4
                    y = y + c/4
                    drawLine(ctx.cgContext, x, y, x + c/2, y - c/2, false, UIColor.black.cgColor)

                case 1:
                    x = x + c/2
                    y = y + c/4
                    drawLine(ctx.cgContext, x, y, x , y - c/2, false, UIColor.black.cgColor)

                case 2:
                    x = x + 3*c/4
                    y = y + c/4
                    drawLine(ctx.cgContext, x, y, x + c/2, y - c/2, false, UIColor.black.cgColor)

                case 10:
                    x = x - c/4
                    y = y + c/2
                    drawLine(ctx.cgContext, x, y, x + c/2, y, false, UIColor.black.cgColor)

                case 12:
                    x = x + 3*c/4
                    y = y + c/2
                    drawLine(ctx.cgContext, x, y, x + c/2, y, false, UIColor.black.cgColor)

                case 20:
                    x = x - c/4
                    y = y + 5*c/4
                    drawLine(ctx.cgContext, x, y, x + c/2, y - c/2, false, UIColor.black.cgColor)
                case 21:
                    x = x + c/2
                    y = y + 3*c/4
                    drawLine(ctx.cgContext, x, y, x , y + c/2, false, UIColor.black.cgColor)

                case 22:
                    x = x + 3*c/4
                    y = y + 3*c/4
                    drawLine(ctx.cgContext, x, y, x + c/2, y + c/2, false, UIColor.black.cgColor)

                default :
                    print ("Bug:")
                }
            }
            if (labelSolved != nil) { labelSolved.isHidden = true }
            if nbGoodColor == penta.width!*penta.height! {
                //print ("All color are OK !!!!")
                currentPentaSolved = true
                if labelSolved == nil { labelSolved = UIButton()
                    let top = self.view.layoutMarginsGuide.topAnchor
                    let trailing = self.view.layoutMarginsGuide.trailingAnchor
                    self.view.addSubview(labelSolved)
                    let middle = self.view.layoutMarginsGuide.centerXAnchor
                    
                    labelSolved.topAnchor.constraint(equalTo: top, constant: 100).isActive = true
                    labelSolved.heightAnchor.constraint(equalToConstant: 40).isActive = true
                    labelSolved.widthAnchor.constraint(equalToConstant: screenWidth).isActive = true
                    labelSolved.centerXAnchor.constraint(equalTo: middle, constant: 0).isActive = true
                    labelSolved.trailingAnchor.constraint(equalTo: trailing, constant: 0).isActive = true
                    labelSolved.titleLabel?.textAlignment = .center
                    labelSolved.setTitle((currentPenta?.name)! + " IS SOLVED", for: [])
                    labelSolved.backgroundColor = UIColor(rgb: 0x00ff00)
                    labelSolved.setTitleColor(UIColor.black, for: UIControlState.normal)
                    
                    labelSolved.translatesAutoresizingMaskIntoConstraints = false
                    labelSolved.tag = (butType.label).rawValue
                    labelSolved.addTarget(self, action: #selector(buttonNBAction), for: .touchUpInside)
                } else {
                    labelSolved.isHidden = false
                }
                //labelSolved.setTitleColor(UIColor.green, for: UIControlState.normal)
                //labelSolved.setTitle("Solved", for: UIControlState.normal)
            } else {
                if labelSolved != nil {
                    labelSolved.isHidden = true
                }
                currentPentaSolved = false
            }
        }
        
        myviewImage.image = img
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
            context.setLineDash(phase: 0, lengths: [1, 2])
        } else {
            context.setLineWidth(2)
            context.setLineDash(phase: 0, lengths: [])
        }
        context.setStrokeColor(color)
        context.strokePath()
    }
    
    
    public func setVSet(vset:[Set<Int>], index:Int) {
        if !ldefine.helpButtonValue {
        vSet = vset
        }
        globalIndex = index
    }
    func savePreferences () {
        if globalIndex >= 0 { globalUserGameData.totale![globalIndex].vSet = vSet
            globalUserGameData.totale![globalIndex].completed = currentPentaSolved

        }
        else {
            let currBackup:ABackup = ABackup()
            currBackup.name = currentPenta?.name
            currBackup.vSet = vSet
            currBackup.completed = currentPentaSolved
            
            globalUserGameData.totale?.append(currBackup)
            globalIndex = (globalUserGameData.totale?.count)! - 1
        }
        
        
        // let documents = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first!
        let paths = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)
        let myDataPath = paths[0].appending("/globalUserData.json")
        
        if !ldefine.doNotSave && !ldefine.forceDoNotSave {
            /*
             * In some cases ( failure to read the json / network issue with git)
             * we dont have a coherent set of penta.
             * we know that from doNotSave param
             * In this case we dont want to save corrupted data. )
             */
            let url = URL(fileURLWithPath:myDataPath)
            let encodedData = try? JSONEncoder().encode(globalUserGameData)
            try? encodedData?.write(to: url)
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
        
        //self.view.addGestureRecognizer(longPress)
        
        
        let longPress = UILongPressGestureRecognizer(target: self, action: #selector(longPressAction(_:)))
        longPress.minimumPressDuration = 2.0;
        button.addGestureRecognizer(longPress)

        
        
        return button
    }
    
    func addNButton (x:CGFloat, y:CGFloat, width:CGFloat, height:CGFloat, color: UIColor, tag:Int) {
        let myImage:UIImage = UIImage(named: "transparent.png")!
        let button = UIButton(type: UIButtonType.custom)
        let rect = CGRect(x:x,y:y+CGFloat(minW),width:width,height:height)
        button.frame = rect
        button.setImage(myImage, for: UIControlState.normal)
        button.backgroundColor = color
        button.addTarget(self, action: #selector(buttonNBAction), for: .touchUpInside)
        button.tag = tag
        self.view.addSubview(button)
        
        
    }
    
    enum butType:Int {
        case back = 999
        case Undo = 998
        case Redo = 997
        case Reset = 996
        case label = 995
        case regexp = 994
    }
    
    func addBackButton() {
        
        let butWidth = 40
        
        
        let tImages:[butType:UIImage] = [ .back:UIImage(named: "back40.png")!,.Undo:UIImage(named: "undo40.png")!,.Redo:UIImage(named: "redo40.png")!,.Reset:UIImage(named: "reset40.png")!, .regexp:UIImage(named: "replace40.png")!]

        var trailing = self.view.layoutMarginsGuide.trailingAnchor
        let bottom = self.view.layoutMarginsGuide.bottomAnchor
        
        for i in tImages {
            let tempButton = UIButton()
            self.view.addSubview(tempButton)
            
            //tempButton.setTitle(myTitle, for: UIControlState.normal)
            tempButton.setImage(tImages[i.key], for: [])
            tempButton.backgroundColor = UIColor.white
            //tempButton.setTitleColor(UIColor.blue, for: UIControlState.normal)
            tempButton.layer.cornerRadius = 3
            //tempButton.sizeToFit()
            tempButton.widthAnchor.constraint(equalToConstant: CGFloat(butWidth))
            //tempButton.sizeThatFits(CGSize(width: 20, height: 20))
            tempButton.translatesAutoresizingMaskIntoConstraints = false
            tempButton.bottomAnchor.constraint(equalTo: bottom, constant: 0).isActive = true
            tempButton.trailingAnchor.constraint(equalTo: trailing, constant: -20).isActive = true
            trailing = tempButton.layoutMarginsGuide.leadingAnchor
            tempButton.tag = i.key.rawValue
            tempButton.addTarget(self, action: #selector(buttonNBAction), for: .touchUpInside)
        }
        labelTitlePenta = UIButton()
        let top = self.view.layoutMarginsGuide.topAnchor
        trailing = self.view.layoutMarginsGuide.trailingAnchor
        self.view.addSubview(labelTitlePenta)
        let middle = self.view.layoutMarginsGuide.centerXAnchor
        
        labelTitlePenta.topAnchor.constraint(equalTo: top, constant: 0).isActive = true
        labelTitlePenta.widthAnchor.constraint(equalToConstant: screenWidth/2).isActive = true
        labelTitlePenta.centerXAnchor.constraint(equalTo: middle, constant: 0).isActive = true
        labelTitlePenta.trailingAnchor.constraint(equalTo: trailing, constant: 0).isActive = true
        labelTitlePenta.titleLabel?.textAlignment = .center
        if (currentPenta?.differences)! != [] {
            labelTitlePenta.setTitle((currentPenta?.name)!, for: [])
            
        } else {
            labelTitlePenta.setTitle(currentPenta?.name, for: [])
        }
        labelTitlePenta.backgroundColor = UIColor(rgb: 0xcccccc)
        labelTitlePenta.titleLabel?.textColor = UIColor.black
        labelTitlePenta.translatesAutoresizingMaskIntoConstraints = false
        labelTitlePenta.tag = (butType.label).rawValue
        labelTitlePenta.addTarget(self, action: #selector(buttonNBAction), for: .touchUpInside)
    }
    @IBAction func buttonNBAction(_ sender: UIButton) {
        var number:Int = sender.tag
        
        switch number {
        case butType.label.rawValue:
            sender.isHidden = true
            return
        case butType.back.rawValue:
            dismiss(animated: true, completion: nil)
            return
        case butType.Reset.rawValue:
            selectedValue = -1
            for i  in 0..<vSet.count {
                if vSet[i].count == 1 && (vSet[i].first)! < 0 { continue } else {
                    vSet[i] = []
                }
            }
            
            savePreferences()
            drawPenta(penta: currentPenta!, valSelected: -1, darker:[])
            pastAction = []
            futurAction = []
            return
        case butType.regexp.rawValue:
                if myPickerView == nil {
                    createPicker()
                    
                } else {
                    if !myPickerView.isHidden {
                        for i in 0..<vSet.count {
                            let value = myPickerView.selectedRow(inComponent: 0)+6
                            let replace = myPickerView.selectedRow(inComponent: 1)
                            if replace != 0 && vSet[i].contains(value) {
                                vSet[i].remove(value)
                                vSet[i].insert(replace)
                            }
                        }
                    }
                    myPickerView.isHidden = !myPickerView.isHidden
                    selectedValue = -1
                    drawPenta(penta: currentPenta!, valSelected: -1, darker:[])
                    savePreferences()

                    return
                }

        default:
            var thisAction:action? = nil
            
            if (number == butType.Undo.rawValue) {
                if pastAction.count != 0 {
                    thisAction = pastAction.popLast()
                    futurAction.append(thisAction!)
                } else {
                    break
                }
            } else
                
                if (number == butType.Redo.rawValue) {
                    if futurAction.count != 0 {
                        thisAction = futurAction.popLast()
                        pastAction.append(thisAction!)
                    } else {
                        break
                    }
                } else {
                    pastAction.append(action(cell: selectedValue,value: number))
                    futurAction = []
            }
            if thisAction != nil {
                selectedValue = (thisAction?.cell)!
                number = (thisAction?.value)!
            }
            //print ("Number = \(number) in buttonNBAction")
            // No cell selected in the penta ... nothing to do
            if selectedValue == -1 {
                //print("Nothing to do - no value selected")
                return
            }
            
            // This is a predefine value .... no action accepted
            if vSet[selectedValue].count == 1 && vSet[selectedValue].first! < 0 {
                //print("We dont do anything since it's a predefined value")
                return
            }
            // Lets see if the value is already present
            if vSet[selectedValue].contains(number) {
                // lets remove it
                vSet[selectedValue].remove(number)
                //armand
            } else {
                vSet[selectedValue].insert(number)
            }
            drawPenta(penta: currentPenta!, valSelected: selectedValue, darker:sameAreaCells[selectedValue]
            )
            //print ("number = \(number)")

            savePreferences()
        }
    }
    
    
    @IBAction func pickerAction(_ sender: UIPickerView) {
    //print ("Action PICKER")
        sender.isHidden = true
    }
    
    @objc func longPressAction(_ sender: UIGestureRecognizer) {
        
        if sender.state == .ended {
            print("Long press Ended")
        } else if sender.state == .began {
            print("Long press detected")
            let tag:Int = (sender.view?.tag)!
            if tag >= 0 && tag < (currentPenta?.width)!*(currentPenta?.height)! {
                if vSet[tag].count > 0 && vSet[tag].first! > 0 {
                    vSet[tag] = []
                }
                savePreferences()
                drawPenta(penta: currentPenta!, valSelected: -1, darker: [])
            }

        }
    }
    @IBAction func buttonAction(_ sender: UIButton) {
        var areaCells:Set<Int> = []
        
        let buttonID:Int = sender.tag
        switch buttonID {
        case butType.back.rawValue:
            // We did press Back button
            
            dismiss(animated: true, completion: nil)
            return
        case butType.Undo.rawValue:
            // Undo
            break
            
        case butType.Redo.rawValue:
            // Redo
            break
        default:
            if selectedValue != buttonID {
                selectedValue = buttonID
                areaCells = sameAreaCells[selectedValue]
            } else {
                selectedValue = -1
                areaCells = []
            }
            drawPenta(penta: currentPenta!, valSelected: selectedValue, darker: areaCells)
        }
    }
    
    // MARK: Picker functions
    
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 2
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        if component == 0 {
            return whatTag.count
        } else { return 6 }
    }
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        if component == 0 {
            return whatTag[row]
        } else {
            if (row == 0) {
                return "-"
            } else {
            return String(row)
            }
        }
        
    }
    @IBOutlet var myPickerView: UIPickerView!
    func createPicker() {
        myPickerView = UIPickerView(frame: CGRect(x: 100, y: 100, width: 20, height: 40))
        myPickerView.self.delegate = self as UIPickerViewDelegate
        myPickerView.self.dataSource = self as UIPickerViewDataSource
        self.view.addSubview(myPickerView)
        let bottom = self.view.layoutMarginsGuide.bottomAnchor
        let trailing = self.view.layoutMarginsGuide.trailingAnchor
        let h = screenHeight/12
        
        myPickerView.bottomAnchor.constraint(equalTo: bottom, constant:-20).isActive = true
        myPickerView.trailingAnchor.constraint(equalTo: trailing, constant: 0).isActive = true
        myPickerView.backgroundColor = UIColor.brown
        myPickerView.translatesAutoresizingMaskIntoConstraints = false
        myPickerView.target(forAction: #selector(pickerAction), withSender: myPickerView )
        myPickerView.widthAnchor.constraint(equalToConstant: screenWidth/4).isActive = true
        myPickerView.heightAnchor.constraint(equalToConstant: h).isActive = true
    }

    
    
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
