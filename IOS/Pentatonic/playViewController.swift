//
//  playViewController.swift
//  Pentatonic
//
//  Created by Armand Hornik on 19/02/2018.
//  Copyright © 2018 Armand Hornik. All rights reserved.
//

import UIKit

//MARK: - Some Classes used by the playViewController
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
class playViewController: UIViewController ,UIPickerViewDelegate, UIPickerViewDataSource, UIPickerViewAccessibilityDelegate, UIScrollViewDelegate{
    //MARK: - The variables used by the class
    var arrayPlayButtons:[MYUIButton] = []
    var initialized:Bool = false
    var currentPenta:APenta? = nil
    var initialX:Int = 0
    var initialY:Int = 0
    var sizeBut:Int = 0
    var sizeFont:CGFloat = 0
    var petiteFont:CGFloat = 0
    let blue = 0
    let black = 1
    let red = 2
    var counter = 0
    var maxCounter = 0
    var FontColor:[UIColor] = [ ldefine.currentTheme[prefsJSON.LColor.blue]!,ldefine.currentTheme[prefsJSON.LColor.black]!,ldefine.currentTheme[prefsJSON.LColor.red]!]
    var stopSolveRequested = false
    var listController:listNiveauViewController?
    
    var old = true
    
    var dxl:CGFloat = 0
    var dxm:CGFloat = 0
    var dxr:CGFloat = 0
    var dyu:CGFloat = 0
    var dym:CGFloat = 0
    var dyd:CGFloat = 0
    var confirmReset:UIAlertController? = nil
    var confirmExit:UIAlertController? = nil
    var sizeLine:CGFloat = 0
    
    var currentPentaSolved:Bool = false
    var globalIndex = -1
    
    var screenWidth:CGFloat = 0
    var screenHeight:CGFloat = 0
    var minW:Int = 0
    
    // array for each cell, contains a Set of cell with a constraints ( different value mandatory ) to the cell ,
    // In or outside the regions
    var sameAreaCells:[Set<Int>] = [[]]
    
    // Number of element in the region the cell belongs to
    var regionCount :[Int] = []
    
    // vSet will contain the original values (negative)
    // and set of value user want to pre-select
    
    var vSet :[Set<Int>] = [[]]
    
    // Dictionnary containing all the regions, with the form of Set of Int
    // Key is used to differentiate areas between them
    
    var arrayRegionSet : [String:Set<Int>] = [:]
    
    var selectedValue:Int = -1
    var whatTag = ["0","1","2","3","4","5","a","b","c","d","e","x","y","z","v","w"]
    var currentTags = ["0":0,"1":1,"2":2,"3":3,"4":4,"5":5,"a":6,"b":7,"c":8,"d":9,"e":10,"x":11,"y":12,"z":13,"v":14,"w":15]
    var actualTags:Set<String> = []
    
    @IBOutlet var zoomedImageView: MYUIView!
    
    
    var pastAction:[action] = []
    var futurAction:[action] = []
    
    @IBOutlet var myBackgroundImage: UIImageView!
    @IBOutlet var pentaImageView: UIImageView!
    @IBOutlet var scrollImageView: MYScrollView!
    @IBOutlet var myNumbersImage: UIImageView!
    @IBOutlet var labelTitlePenta:UIButton!
    @IBOutlet var labelSolved:UIButton!
    
    //MARK: - The functions
    
    
    // return 0 if penta is solved
    // return 1 if penta is not solved
    // return 2 if it's not possible to solve it
    
    public func getCounter() -> Int {
        return counter
    }
    
    public func getMaxCounter() -> Int {
        return maxCounter
    }
    public func isPentaSolved (_ penta:APenta, possibleV:[Set<Int>]) -> Int {
        var isPentaSolved = 0
        for value in possibleV {
            if value.count == 0  {
                return 2
            } else if value.count != 1 {
                isPentaSolved = 1
            }
        }
        if isPentaSolved == 0 {
            for value in 0..<possibleV.count {
                if !(vSet[value].count == 1 && vSet[value].first! <= 0 ) {
                    vSet[value] = [possibleV[value].first!]
                }
            }
        }
        return isPentaSolved
    }
    
    
    
    /// setPenta :
    /// This function set default value for the controller to be able to display the Penta
    /// This function must be called before any invocation of this controller.
    ///
    /// - Parameter penta: This is the next penta to be display and played in this controller
    ///
    
    public func stopSolve() {
        stopSolveRequested = true
        
    }
    public func setListCtrl(_ Controller:listNiveauViewController){
        self.listController = Controller
    }
    
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
        if sizeBut < 30 {
            sizeLine = 2
        } else if sizeBut < 70 {
            sizeLine = 3
        } else { sizeLine = 3.5 }
        sizeFont = CGFloat(sizeBut) * 0.8
        
        petiteFont = CGFloat(sizeBut) * 0.8 / 3
        
        dxm = 0-CGFloat(sizeBut) / 4.8
        dxl = dxm + CGFloat(sizeBut) / 2.5
        dxr = dxm - CGFloat(sizeBut) / 2.7
        
        dyd = CGFloat(sizeBut) / 1.5
        dym = CGFloat(sizeBut) / 3.3
        dyu = CGFloat(0.0)
        
        // array for each cell, contains a Set cells in the same regions, but not the cell itself.
        var sameRegionCells:[Set<Int>] = [[]]
        
        
        // array for each cell, contains a Set of cells  touching this one, outside the region
        var otherRegionSameAreaCells:[Set<Int>] = [[]]
        
        //    if no bug : sameAreaCells[i] = otherRegionSameAreaCells[i] union sameRegionCells [i]
        
        var possibleValue : [Set<Int>] = [[]]
        
        
        regionCount = Array(repeating:0,count:size)
        sameAreaCells = Array(repeating:Set<Int>(), count:size)
        sameRegionCells = Array(repeating:Set<Int>(),count:size)
        otherRegionSameAreaCells = Array(repeating:Set<Int>(),count:size)
        
        possibleValue = Array(repeating:Set<Int>(),count:size)
        
        vSet = Array(repeating:Set<Int>(),count:size)
        
        
        
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
                for cell2 in regionSet.value {
                    if cell != cell2 {
                        sameRegionCells[cell].insert(cell2)
                    }
                }
            }
        }
        for i in 0..<sameAreaCells.count {
            sameAreaCells[i].remove(i)
        }
        for i in 0..<otherRegionSameAreaCells.count {
            otherRegionSameAreaCells[i] = sameAreaCells[i]
            otherRegionSameAreaCells[i].formSymmetricDifference(sameRegionCells[i])
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
            let backupSameArea:[Set<Int>] = sameAreaCells
            // let backupSisters:[ASimilarites] = penta.sisters!
            solvePenta(penta, &possibleValue, size, &otherRegionSameAreaCells, &sameRegionCells)
            sameAreaCells = backupSameArea
            //penta.sisters = backupSisters
        }
    }
    
    
    @objc func respondToSwipeGesture(gesture: UIGestureRecognizer) {
        
        print ("Swipe")
    }
    
    fileprivate func addMainZoomedImageView(_ miniImage:UIView) {
        if !ldefine.zoomScrollActivated {
            
            self.view.addSubview(miniImage)
        } else {
            zoomedImageView.addSubview(miniImage)
            //scrollImageView.addSubview(miniImage)
            
            
            scrollImageView.isExclusiveTouch = false
            zoomedImageView.isExclusiveTouch = false
            scrollImageView.delaysContentTouches = false
        }
    }
    func initializePopupS () {
        confirmExit = UIAlertController(title: "Do you really want to go back to Penta Selection ?", message: "Current status will be saved, but you will lose the action's history for this Penta", preferredStyle: UIAlertController.Style.alert)
        confirmReset = UIAlertController(title: "Remove all values on this Penta ?", message: "You will lose any progress on this Penta", preferredStyle: UIAlertController.Style.alert)
        
        confirmExit?.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
        confirmExit?.addAction(UIAlertAction(title: "confirm", style: .default, handler: { action in
            switch action.style{
            case .default:
                print("default")
                self.dismiss(animated: true, completion: nil)
                
            case .cancel:
                print("cancel")
                
            case .destructive:
                print("destructive")
                
            }
        }))
        confirmReset?.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
        confirmReset?.addAction(UIAlertAction(title: "Reset", style: .default, handler: { action in
            switch action.style{
            case .default:
                print("default")
                self.selectedValue = -1
                for i  in 0..<self.vSet.count {
                    if self.vSet[i].count == 1 && (self.vSet[i].first)! < 0 { continue } else {
                        self.vSet[i] = []
                    }
                }
                self.currentPentaSolved = false
                self.saveGameData()
                self.drawPenta(penta: self.currentPenta!, valSelected: -1, darker:[])
                self.pastAction = []
                self.futurAction = []
            case .cancel:
                print("cancel")
                
            case .destructive:
                print("destructive")
                
                
            }}))
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        zoomedImageView = MYUIView()
        
        let image = UIImage(named: "background2.png")!
        myBackgroundImage = UIImageView(image: image)
        
        self.view.addSubview(myBackgroundImage)
        
        initializePopupS()
        
        let trailing = self.view.layoutMarginsGuide.trailingAnchor
        let leading = self.view.layoutMarginsGuide.leadingAnchor
        let top = self.view.layoutMarginsGuide.topAnchor
        let bottom = self.view.layoutMarginsGuide.bottomAnchor
        
        if ldefine.zoomScrollActivated {
            
            scrollImageView = MYScrollView()
            scrollImageView.delegate = self
            scrollImageView.frame = CGRect(x: 0, y: 0, width: screenWidth, height: screenHeight)
            scrollImageView.alwaysBounceVertical = false
            scrollImageView.alwaysBounceHorizontal = false
            scrollImageView.showsVerticalScrollIndicator = true
            scrollImageView.flashScrollIndicators()
            
            scrollImageView.minimumZoomScale = 1.0
            scrollImageView.maximumZoomScale = 2.0
            
            scrollImageView.layer.cornerRadius = 11.0
            scrollImageView.clipsToBounds = false
            scrollImageView.addSubview(zoomedImageView)
            self.view.addSubview(scrollImageView)
        }
        old = ldefine.oldBehaviour
        
        myBackgroundImage.translatesAutoresizingMaskIntoConstraints = false
        
        myBackgroundImage.trailingAnchor.constraint(equalTo: trailing, constant: 0).isActive = true
        myBackgroundImage.leadingAnchor.constraint(equalTo: leading, constant: 0).isActive = true
        myBackgroundImage.topAnchor.constraint(equalTo: top, constant: 0).isActive = true
        myBackgroundImage.bottomAnchor.constraint(equalTo: bottom, constant: 0).isActive = true
        
        
        pentaImageView = UIImageView(frame:CGRect(x: 0, y: 0, width: minW, height: minW))
        addMainZoomedImageView(pentaImageView)
        
        myNumbersImage = UIImageView(frame:CGRect(x:0,y:minW,width:minW,height:(Int(screenHeight) - minW)))
        // Buttons for number should not scroll/zoom
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
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(actionTouchBackground(_:)))
        self.view.addGestureRecognizer(tapGesture)
    }
    @IBAction func actionTouchBackground(_ sender: Any) {
        //print("In ACTION **************")
        removePicker()
        selectedValue = -1
        drawPenta(penta: currentPenta!, valSelected: -1, darker:[])
        
    }
    
    func viewForZooming(in scrollView: UIScrollView) -> UIView? {
        return zoomedImageView
        //        return pentaImageView
    }
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func getStringNButton(_ val:Int) -> String {
        var ret:String
        var array:[String] = []
        array = whatTag
        switch val {
            
        case 1..<array.count:
            ret=array[val]
            
        default:
            ret="?"
        }
        return ret
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
    
    
    
    public func setVSet(vset:[Set<Int>], index:Int) {
        if !ldefine.helpButtonValue {
            vSet = vset
        }
        globalIndex = index
    }
    func saveGameData () {
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
    
    
    // MARK:- Automatic Resolution Tools
    
    /*
     * Functions Used for the resolution of the pentas ( automatic )
     */
    
    fileprivate func solveSisters(_ penta: APenta, _ possibleV: inout [Set<Int>], _ change: inout Bool, _ sameRegionCells: inout [Set<Int>], _ activate:Bool) {
        // add some fake sisters
        if activate {
            for region in arrayRegionSet {
                for cell1 in region.value {
                    for cell2 in sameAreaCells[cell1] {
                        if !sameRegionCells[cell1].contains(cell2) {
                            var local : Set<Int> = []
                            
                            for cell4 in sameRegionCells[cell2] {
                                if !sameAreaCells[cell1].contains(cell4) {
                                    local.insert(cell4)
                                }
                            }
                            var o:Set<Int> = []
                            if local.count != 0 {
                                for i in 1...sameRegionCells[local.first!].count+1 { o.insert(i)}
                            }
                            if local.count == 1 && o.isSuperset(of: possibleV[cell1]) {
                                let lIJ1:IJ = IJ.init(p: penta, index: cell1)
                                let lIJ2:IJ = IJ.init(p: penta, index: local.first!)
                                var done:Bool = false
                                var p1:Bool = false
                                var p2:Bool = false
                                var maxID = 0
                                
                                for sister in penta.sisters! {
                                    maxID = max(sister.id!,maxID)
                                    
                                    if !done {
                                        for pos in sister.positions! {
                                            p2 = p2 || ((pos.i == lIJ1.i) && (pos.j == lIJ1.j))
                                            p1 = p1 || ((pos.i == lIJ2.i) && (pos.j == lIJ2.j))
                                        }
                                        if p1 && p2 {
                                            // both cell are already in a sister
                                            done = true
                                        } else {
                                            
                                            if (p2 || p1) {
                                                // let add a new position to this sister
                                                let S = APoint()
                                                if p1 {
                                                    S.i = lIJ1.i
                                                    S.j = lIJ1.j
                                                } else {
                                                    S.i = lIJ2.i
                                                    S.j = lIJ2.j
                                                }
                                                //print ("*******ADD S\(Optional(S.i)) \(Optional(S.j)) in sister id-\(Optional(sister.id))")
                                                sister.positions?.append(S)
                                                change = true
                                                done = true
                                            }
                                        }
                                    }
                                }
                                if !done {
                                    // was not part of any sister, let's create a new one
                                    let sister:ASimilarites = ASimilarites()
                                    sister.positions = []
                                    let S1 = APoint()
                                    S1.i = lIJ1.i
                                    S1.j = lIJ1.j
                                    
                                    let S2 = APoint()
                                    S2.i = lIJ2.i
                                    S2.j = lIJ2.j
                                    
                                    sister.id = maxID+1
                                    sister.symbol = "."
                                    sister.positions?.append(S1)
                                    sister.positions?.append(S2)
                                    penta.sisters?.append(sister)
                                    //print("*******ADD S1 and S2 in new SISTER")
                                    change = true
                                    //print ("Pass S1:(\(S1.i!),\(S1.j!)) S2:(\(S2.i!),\(S2.j!) \(penta.sisters!)")
                                }
                                
                            }
                        }
                    }
                }
            }
        }
        // change samearea to take into account sisters - no need to alter change value since its always false here and we should evaluate it at each turn
        for sister in penta.sisters! {
            var unionSameArea:Set<Int> = []
            for pos in sister.positions! {
                let lIJ:IJ = IJ.init(p: penta, index: 0)
                let index = lIJ.getIndex(penta, pos.i!, pos.j!)
                unionSameArea = unionSameArea.union(sameAreaCells[index])
            }
            for pos in sister.positions! {
                let lIJ:IJ = IJ.init(p: penta, index: 0)
                let index = lIJ.getIndex(penta, pos.i!, pos.j!)
                sameAreaCells[index] = unionSameArea
            }
        }
        // Treatment of Sisters
        for sister in penta.sisters! {
            var A:Set<Int>? = nil
            var AIndex:Set<Int> = []
            
            for pos in sister.positions! {
                let lIJ:IJ = IJ.init(p: currentPenta!, index: 0)
                let index = lIJ.getIndex(penta, pos.i!, pos.j!)
                if A == nil { A = possibleV[index]}
                A = A?.intersection(possibleV[index])
                AIndex.insert(index)
            }
            for index in AIndex {
                if !possibleV[index].isSubset(of: A!) || !A!.isSubset(of: possibleV[index]) {
                    change = true
                    possibleV[index] = A!
                }
            }
        }
        
    }
    fileprivate func solveDifferences(_ penta: APenta, _ possibleV: inout [Set<Int>]) {
        for difference in penta.differences! {
            let lIJ:IJ = IJ.init(p: penta, index: 0)
            let index1 = lIJ.getIndex(penta, difference.position1!.i!,difference.position1!.j!)
            let index2 = lIJ.getIndex(penta, difference.position2!.i!,difference.position2!.j!)
            var A1:Set<Int> = []
            var A2:Set<Int> = []
            for i in possibleV[index1] {
                A2.insert(i-1)
                A2.insert(i+1)
            }
            possibleV[index2] = A2.intersection(possibleV[index2])
            for i in possibleV[index2] {
                A1.insert(i-1)
                A1.insert(i+1)
            }
            possibleV[index1] = A1.intersection(possibleV[index1])
        }
    }
    
    fileprivate func solveUniqueValues(_ size: Int, _ possibleV: inout [Set<Int>], _ locVSet: inout [Set<Int>], _ change: inout Bool) {
        for i in 0..<size {
            if possibleV[i].count == 1 && locVSet[i].count == 0 {
                locVSet[i]=[possibleV[i].first!]
                change = true
                for j in sameAreaCells[i] {
                    if i != j {
                        possibleV[j].remove(possibleV[i].first!)
                        
                    }
                }
                
            }
        }
    }
    
    fileprivate func solveUniqueInRegionL(_ dicoRegion: [String : Set<Int>], _ possibleV: inout [Set<Int>], _ locVSet: inout [Set<Int>], _ change: inout Bool, _ otherRegionLocal: [Set<Int>]) {
        for regionSet in dicoRegion {
            var local:Set<Int> = []
            for pentaVal in 1...regionSet.value.count {
                // Local is the list of cells which can contain pentaVal
                // if local.count is 1 then only one cell in this region can contain this value
                local = []
                
                for cell in regionSet.value {
                    
                    if possibleV[cell].contains(pentaVal) {
                        local.insert(cell)
                    }
                }
                if local.count == 1 && possibleV[local.first!].count != 1 {
                    if !(locVSet[local.first!].count == 1 && locVSet[local.first!].first! <= 0 ) {
                        possibleV[local.first!] = [ pentaVal]
                        locVSet[local.first!] = [pentaVal]
                        for j in sameAreaCells[local.first!] {
                            if local.first! != j {
                                possibleV[j].remove(pentaVal)
                                
                            }
                            
                        }
                        
                        change = true
                    }
                }
                if local.count >= 1 {
                    var inter:Set<Int> = otherRegionLocal[local.first!]
                    for cell in local {
                        inter = inter.intersection(otherRegionLocal[cell])
                    }
                    for cell in inter {
                        if possibleV[cell].contains(pentaVal) {
                            possibleV[cell].remove(pentaVal)
                            change = true
                        }
                    }
                }
            }
        }
    }
    
    fileprivate func solveOtherNeighbourSisters(_ change: inout Bool, _ dicoRegion: [String : Set<Int>], _ possibleV: inout [Set<Int>]) {
        if change == false {
            for regionSet in dicoRegion {
                
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
                        if possibleV[cell].contains(pentaVal) {
                            possibleV[cell].remove(pentaVal)
                            change = true
                        }
                    }
                }
                //print ("region:\(regionSet.key): all cellse have \(String(describing: neighbourWholeRegion)) as neighbourssubtracting")
            }
            for regionSet in dicoRegion {
                // For every region
                for otherRegionSet in dicoRegion {
                    //compare with every other region
                    if regionSet.key != otherRegionSet.key {
                        if otherRegionSet.value.count >= regionSet.value.count {
                            for cell in regionSet.value {
                                // now check that n-1 Ocell of otherRegionSet are in same area as cell
                                // if this is the case the Ocell N , possible value for OcellN and cell are the same - e.g intersection of the 2 sets
                                let A = sameAreaCells[cell]
                                var n:Int = otherRegionSet.value.count
                                var OKCell:Int = -1
                                for Ocell in otherRegionSet.value {
                                    if A.contains(Ocell) {
                                        n = n-1
                                    } else { OKCell = Ocell}
                                }
                                if n == 1 && ( !possibleV[OKCell].isSubset(of: possibleV[cell]) ||  !possibleV[cell].isSubset(of: possibleV[OKCell])) {
                                    // We got one, it's OKCell
                                    let B = possibleV[OKCell].intersection(possibleV[cell])
                                    
                                    possibleV[OKCell] = B
                                    possibleV[cell] = B
                                    change = true
                                }
                                
                            }
                        }
                    }
                }
            }
        }
    }
    
    fileprivate func solveIntersectionPossibleValues(_ change: inout Bool, _ size: Int, _ possibleV: inout [Set<Int>]) {
        /*
         * if we have 2 cells which are neighbour, and share only 2 values , than none of the common neighbour can share the same value
         * we update change, only if something was changed !!!
         */
        if !change {
            for cell in 0..<size {
                for cell2  in sameAreaCells[cell] {
                    if possibleV[cell].isSuperset(of: possibleV[cell2]) && possibleV[cell2].isSuperset(of: possibleV[cell]) && possibleV[cell].count == 2 {
                        for cell3 in sameAreaCells[cell].intersection(sameAreaCells[cell2]) {
                            if cell3 != cell2 && cell3 != cell {
                                for val in possibleV[cell] {
                                    if possibleV[cell3].contains(val) {
                                        possibleV[cell3].remove(val)
                                        change = true
                                    }
                                }
                            }
                        }
                        
                    }
                }
            }
        }
    }
    
    
    
    
    fileprivate func solveUniqueInRegion(_ possibleValue: inout [Set<Int>], _ change: inout Bool, _ otherRegionSameAreaCells: inout [Set<Int>]) {
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
                    if !(vSet[local.first!].count == 1 && vSet[local.first!].first! <= 0 ) {
                        possibleValue[local.first!] = [ pentaVal]
                        vSet[local.first!] = [pentaVal]
                    }
                    for j in sameAreaCells[local.first!] {
                        if local.first! != j {
                            possibleValue[j].remove(pentaVal)
                            
                        }
                        
                    }
                    
                    change = true
                }
                if local.count >= 1 {
                    var inter:Set<Int> = otherRegionSameAreaCells[local.first!]
                    for cell in local {
                        inter = inter.intersection(otherRegionSameAreaCells[cell])
                    }
                    for cell in inter {
                        if possibleValue[cell].contains(pentaVal) {
                            possibleValue[cell].remove(pentaVal)
                            change = true
                        }
                    }
                }
            }
        }
    }
    
    
    
    fileprivate func solvePenta(_ penta: APenta, _ possibleValue: inout [Set<Int>], _ size: Int, _ otherRegionSameAreaCells: inout [Set<Int>], _ sameRegionCells:inout [Set<Int>]) {
        /* use extension of sameArea to sisters */
        
        
        
        var change=true
        while change {
            change=false
            
            solveSisters(penta, &possibleValue, &change,&sameRegionCells,true)
            solveDifferences(penta, &possibleValue)
            
            solveUniqueValues(size, &possibleValue, &vSet, &change)
            solveUniqueInRegionL(arrayRegionSet,&possibleValue, &vSet, &change, otherRegionSameAreaCells)
            solveOtherNeighbourSisters(&change,arrayRegionSet, &possibleValue)
            solveIntersectionPossibleValues(&change, size, &possibleValue)
            
            if change == false && isPentaSolved(penta, possibleV: possibleValue) == 0
            {
                print ("*******PENTA IS SOLVED")
                return
            } else {
                if change == false {
                    maxCounter = size
                    
                    for value in 0..<possibleValue.count {
                        counter = value
                        print ("******* try N \(value)/\(size)")
                        listController?.nc.post(name: Notification.Name("notifPROGRESSBar"), object: nil, userInfo:["counter":counter, "max":size])
                        if stopSolveRequested {
                            stopSolveRequested = false
                            return
                        }
                        let save = possibleValue[value]
                        if possibleValue[value].count > 1 {
                            let backupSisters = penta.sisters
                            possibleValue[value] = [save.first!]
                            let backupSameAreaCells = sameAreaCells
                            let result = solvePentaRecursive(penta, dicoRegion: arrayRegionSet, otherRegionLocal: otherRegionSameAreaCells, sameRegionCells: &sameRegionCells,locVSetP: vSet, possibleVP: possibleValue, level: 1)
                            sameAreaCells = backupSameAreaCells
                            penta.sisters = backupSisters
                            if result == 0
                            {
                                print ("*****THIS TIME IT IS SOLVED YEAHHH")
                                if !(vSet[value].count == 1 && vSet[value].first! <= 0 ) {
                                    vSet[value] = [save.first!]
                                }
                                return
                            } else {
                                if result == 2 {
                                    //print ("*****This is abolsutely impossible - backtrack")
                                    possibleValue[value] = save
                                } else {
                                    //print ("*****TOO BAD, one level was not enough")
                                    possibleValue[value] = save
                                }
                            }
                        }
                    }
                }
            }
        }
        
    }
    
    // solvePentaRecursive : try to solve a penta by applying all rules it knows.
    // Can call itself recursively to check some hypothesis.
    // will return false if the penta did reach an impossible case ( one cell of Possible value is empty )
    // will return true if still possible to be solve ( or eventually solved )
    
    
    public func solvePentaRecursive (_ penta:APenta, dicoRegion:[String:Set<Int>], otherRegionLocal:[Set<Int>], sameRegionCells:inout [Set<Int>], locVSetP:[Set<Int>], possibleVP:[Set<Int>], level:Int) -> Int
    {
        let width = penta.width
        let height = penta.height
        let size:Int = width! * height!
        var locVSet = locVSetP
        var possibleV = possibleVP
        if level > ldefine.levelMax {
            return 1
            
        }
        
        var change=true
        while change {
            change=false
            solveSisters(penta, &possibleV, &change,&sameRegionCells,false)
            solveDifferences(penta, &possibleV)
            solveUniqueValues(size, &possibleV, &locVSet, &change)
            solveUniqueInRegionL(dicoRegion, &possibleV, &locVSet, &change, otherRegionLocal)
            solveOtherNeighbourSisters(&change, dicoRegion, &possibleV)
            solveIntersectionPossibleValues(&change, size, &possibleV)
            if change == false && isPentaSolved(penta, possibleV: possibleV) == 0
            {
                print ("*******PENTA IS in SOLVED in recursion")
                return 0
            } else {
                if change == false {
                    for value in 0..<possibleV.count {
                        let save = possibleV[value]
                        if possibleV[value].count > 1 {
                            for v in save {
                                possibleV[value] = [v]
                                let backupSameAreaCells = sameAreaCells
                                let backupSisters = penta.sisters
                                let result = solvePentaRecursive(penta, dicoRegion: dicoRegion, otherRegionLocal: otherRegionLocal, sameRegionCells: &sameRegionCells, locVSetP: locVSet, possibleVP: possibleV, level:level+1)
                                sameAreaCells = backupSameAreaCells
                                penta.sisters = backupSisters
                                if result == 0
                                {
                                    print ("Penta was solved at level \(level)")
                                    if !(vSet[value].count == 1 && vSet[value].first! <= 0)  { vSet[value] = [v] }
                                    return 0
                                } else {
                                    if result == 2 {
                                        // backtrack - this is not an acceptable solution
                                        possibleV[value].remove(v)
                                        possibleV[value] = save
                                    } else {
                                        possibleV[value] = save
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        
        
        
        return isPentaSolved(penta, possibleV: possibleVP)
    }
    
    
    //MARK: - Drawing functions
    /*
     * Drawing Functions
     */
    
    
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
        let listSymbol:[String] = [ "-", "~", "±", "§", "^", "#","&","*","+","į","k","l","n","p","o","p","q","r","s","u","f","g","h"," "]
        for sister in penta.sisters! {
            var symbol = sister.symbol!
            symbol = listSymbol[index]
            // if we are out of symbol .... use the last one
            if index < listSymbol.count { index = (index+1)%listSymbol.count } else { index = listSymbol.count - 1 }
            // since symbol is not yet correctly initialized in json, lets use our OWN
            // TBD
            for position in sister.positions! {
                listSist[position.i!*width + position.j!] = symbol
            }
        }
        let renderer = UIGraphicsImageRenderer(size: CGSize(width: minW, height: minW))
        var nbGoodColor = 0
        let img = renderer.image { ctx in
            let listColor = [ldefine.currentTheme[prefsJSON.LColor.dark]?.cgColor,
                             ldefine.currentTheme[prefsJSON.LColor.light]?.cgColor,
                             ldefine.currentTheme[prefsJSON.LColor.lighter]?.cgColor]
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
            var atts = [NSAttributedString.Key.font: UIFont.systemFont(ofSize: sizeFont),NSAttributedString.Key.foregroundColor:UIColor.black]
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
                        atts = [NSAttributedString.Key.font: UIFont.systemFont(ofSize: sizeFont),                  NSAttributedString.Key.foregroundColor: FontColor[black]]
                        valeur = -valeur
                        nbGoodColor = nbGoodColor + 1
                    } else {
                        // blue or red
                        var col:UIColor = FontColor[blue]
                        if vSet[index].count == 1 && vSet[index].first! > regionCount[index]  && vSet[index].first! < 6 {
                            col = FontColor[red]
                        } else {
                            for cellIndex in sameAreaCells[index] {
                                
                                if cellIndex != index && vSet[cellIndex].count == 1 && abs(vSet[cellIndex].first!) == abs(valeur) {
                                    col = FontColor[red]
                                }
                                
                            }
                        }
                        if col == FontColor[blue] && vSet[index].first! < 6{
                            nbGoodColor = nbGoodColor + 1
                        }
                        atts = [NSAttributedString.Key.font: UIFont.systemFont(ofSize: sizeFont),                  NSAttributedString.Key.foregroundColor: col]
                        
                    }
                    (getStringNButton(valeur) as NSString).draw(at: CGPoint(x: x, y: y), withAttributes: atts)
                    
                } else {
                    atts = [NSAttributedString.Key.font: UIFont.systemFont(ofSize: petiteFont)]
                    
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
                    atts = [NSAttributedString.Key.font: UIFont.systemFont(ofSize: petiteFont*1.4)]
                    
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
                    y = y - c/4
                    drawLine(ctx.cgContext, x, y, x + c/2, y + c/2, false, UIColor.black.cgColor)
                    
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
                    labelSolved.setTitleColor(UIColor.black, for: UIControl.State.normal)
                    
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
        
        pentaImageView.image = img
        //self.view.sendSubview(toBack: pentaImageView)
        zoomedImageView.sendSubview(toBack: pentaImageView)
        //self.view.bringSubview(toFront: zoomedImageView)
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
            context.setLineWidth(sizeLine)
            context.setLineDash(phase: 0, lengths: [])
        }
        context.setStrokeColor(color)
        context.strokePath()
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
        let atts = [NSAttributedString.Key.font:
            UIFont.systemFont(ofSize: min(CGFloat(widthNBut),CGFloat(heightNBut))*0.8),
                    NSAttributedString.Key.foregroundColor:UIColor.black]
        
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
    
    func showTheGrid(_ width:Int, _ height:Int) {
        
        var tag=0
        for j in 0..<height {
            for i in 0..<width {
                arrayPlayButtons.append(addButton(x: initialX+i*sizeBut, y: initialY+j*sizeBut, size: sizeBut, color: .clear, tag: tag))
                tag=tag+1
            }
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
    func addButton (x:Int, y:Int, size:Int, color: UIColor, tag:Int) -> MYUIButton {
        let myImage:UIImage = UIImage(named: "transparent.png")!
        let button = MYUIButton(type: UIButton.ButtonType.custom)
        let rect = CGRect(x:x,y:y,width:size,height:size)
        button.frame = rect
        button.setImage(myImage, for: UIControl.State.normal)
        button.backgroundColor = color
        button.addTarget(self, action: #selector(buttonAction), for: .touchUpInside)
        button.tag = tag
        addMainZoomedImageView(button)
        // self.view.addSubview(button)
        
        //self.view.addGestureRecognizer(longPress)
        
        
        let longPress = UILongPressGestureRecognizer(target: self, action: #selector(longPressAction(_:)))
        longPress.minimumPressDuration = 2.0;
        button.addGestureRecognizer(longPress)
        
        
        
        return button
    }
    
    func addNButton (x:CGFloat, y:CGFloat, width:CGFloat, height:CGFloat, color: UIColor, tag:Int) {
        let myImage:UIImage = UIImage(named: "transparent.png")!
        //myImage = UIImage(named: "rect_rose.png")!
        //TBD TBC JUST TO TEST
        let button = UIButton(type: UIButton.ButtonType.custom)
        let rect = CGRect(x:x,y:y+CGFloat(minW),width:width,height:height)
        button.frame = rect
        button.setImage(myImage, for: UIControl.State.normal)
        button.backgroundColor = color
        button.addTarget(self, action: #selector(buttonNBAction), for: .touchUpInside)
        button.tag = tag
        //addViewToMainImage(button)
        self.view.addSubview(button)
        
        
    }
    
    enum butType:Int {
        case Erase = 999
        case back = 991
        case Undo = 997
        case Redo = 998
        case Reset = 990
        case label = 995
        case regexp = 1000
    }
    func sortDico (_ k1: (key:butType,value:UIImage),_ k2: (key:butType,value:UIImage)) -> Bool {
        
        return k1.key.rawValue >= k2.key.rawValue
    }
    
    func addBackButton() {
        
        let butWidth = 40
        
        
        let tImages:[butType:UIImage] = [ .back:UIImage(named: "back40.png")!,.Undo:UIImage(named: "undo40.png")!,.Redo:UIImage(named: "redo40.png")!,.Reset:UIImage(named: "reset40.png")!, .regexp:UIImage(named: "replace40.png")!, .Erase:UIImage(named:"X40.png")!]
        

        var trailing = self.view.layoutMarginsGuide.trailingAnchor
        let bottom = self.view.layoutMarginsGuide.bottomAnchor
        
        for i in tImages.sorted(by: sortDico(_:_:)) {
            let tempButton = UIButton()
            // no zoom / scroll for the back buttons
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
        
        labelTitlePenta.setTitle(currentPenta?.name, for: [])
        
        labelTitlePenta.backgroundColor = UIColor(rgb: 0xcccccc)
        labelTitlePenta.titleLabel?.textColor = UIColor.black
        labelTitlePenta.translatesAutoresizingMaskIntoConstraints = false
        labelTitlePenta.tag = (butType.label).rawValue
        labelTitlePenta.addTarget(self, action: #selector(buttonNBAction), for: .touchUpInside)
    }
    
    @IBAction func buttonNBAction(_ sender: UIButton) {
        var number:Int = sender.tag
        
        switch number {
        case butType.Erase.rawValue:
            if selectedValue != -1 {
                for value in vSet[selectedValue] {
                    pastAction.append(action(cell:selectedValue, value: value))
                    futurAction = []
                }
                vSet[selectedValue] = []
                selectedValue = -1
                saveGameData()
                drawPenta(penta: currentPenta!, valSelected: -1, darker: [])
            }
            return
        case butType.label.rawValue:
            sender.isHidden = true
            return
        case butType.back.rawValue:
            if currentPentaSolved  || (futurAction.isEmpty && pastAction.isEmpty ){
                self.dismiss(animated: true, completion: nil)
            } else {
                self.present(confirmExit!, animated: true, completion: nil)
            }
            return
        case butType.Reset.rawValue:
            removePicker()
            self.present(confirmReset!, animated: true, completion: nil)
            
            return
        case butType.regexp.rawValue:
            
            if myPickerView == nil {
                createPicker()
                
            } else {
                var array:Array<String> = []
                for tag in actualTags {
                    array.append(tag)
                }
                array.sort()
                
                for i in 0..<vSet.count {
                    if array.count != 0 {
                        let value = currentTags[array[myPickerView.selectedRow(inComponent: 0)]]
                        
                        
                        let replace = myPickerView.selectedRow(inComponent: 1)
                        if replace != 0 && vSet[i].contains(value!) {
                            vSet[i].remove(value!)
                            pastAction.append(action(cell:i, value: value!))
                            vSet[i].insert(replace)
                            pastAction.append(action(cell:i, value: replace))
                            futurAction = []
                            
                        }
                    }
                }
                removePicker()
                selectedValue = -1
                drawPenta(penta: currentPenta!, valSelected: -1, darker:[])
                saveGameData()
                
                return
            }
            
        default:
            var thisAction:action? = nil
            removePicker()
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
            
            saveGameData()
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
                saveGameData()
                drawPenta(penta: currentPenta!, valSelected: -1, darker: [])
            }
            
        }
    }
    @IBAction func buttonAction(_ sender: UIButton) {
        var areaCells:Set<Int> = []
        //print("tag = \(sender.tag)")
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
    
    // MARK: - Picker functions
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 2
    }
    
    func numberActiveVariable () -> Int {
        var allValue:Set<String> = []
        
        for mySet in vSet {
            for value in mySet {
                if value > 5 {
                    //print (whatTag[value])
                    allValue.insert ( whatTag[value])
                }
            }
        }
        //print ("exit numberActiveVariables = \(allValue.count)")
        actualTags = allValue
        return allValue.count
    }
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        if component == 0 {
            return numberActiveVariable()
        } else { return 6 }
    }
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        var array:Array<String> = []
        for tag in actualTags {
            array.append(tag)
        }
        array.sort()
        if component == 0 {
            return array[row]
        } else {
            if (row == 0) {
                return "-"
            } else {
                return String(row)
            }
        }
        
    }
    
    
    func pickerView(_ pickerView: UIPickerView, rowHeightForComponent component: Int) -> CGFloat {
        return max(20,sizeFont)
    }
    
    func pickerView(_ pickerView: UIPickerView, viewForRow row: Int, forComponent component: Int, reusing view: UIView?) -> UIView{
        var array:Array<String> = []
        for tag in actualTags {
            array.append(tag)
        }
        array.sort()
        
        var str:String
        
        if component == 0 {
            str = array[row]
        } else {
            if (row == 0) {
                str = "-"
            } else {
                str = String(row)
            }
        }
        
        var label = view as! UILabel?
        if label == nil {
            label = UILabel()
        }
        
        label?.font = UIFont.systemFont(ofSize: max(20,sizeFont))
        label?.text =  str
        label?.textAlignment = .center
        return label!
        
    }
    
    
    
    @IBOutlet var execBut: UIButton!
    @IBOutlet var myPickerView: UIPickerView!
    
    func removePicker () {
        if myPickerView != nil {
            if (execBut != nil) { execBut.isHidden = true }
            myPickerView.isHidden = true
            self.view.willRemoveSubview(myPickerView)
            if (execBut != nil) {self.view.willRemoveSubview(execBut)}
            execBut = nil
            myPickerView = nil
        }
    }
    
    func createPicker() {
        var bottom:NSLayoutYAxisAnchor
        var trailing:NSLayoutXAxisAnchor
        var h:CGFloat
        var w:CGFloat
        var x:CGFloat,y:CGFloat
        var width:CGFloat,height:CGFloat
        var AnchorTrailing:CGFloat
        var AnchorBottom:CGFloat
        var constraints:Bool = true
        if myPickerView != nil || numberActiveVariable() == 0 {
            return
        }
        
        if old {
            x=100 // Default X position for picker
            y=100 // Default Y position for picker
            width = 20  // Default width for picker
            height = 40 // Default height for picker
            h = screenHeight/12 // Height Anchor
            w = screenWidth/4 // Width Anchor
            AnchorTrailing = 0
            AnchorBottom = -width
        } else {
            constraints = false
            x=20 // Default X position for picker
            y=20 // Default Y position for picker
            width = max(40,sizeFont*4)  // Default width for picker
            height = max(25,sizeFont)  // Default height for picker
            h = height*1.5   // Heigth Anchor
            w = width   // Width Anchor
            AnchorTrailing = -(screenWidth - 2 * w)/2
            AnchorBottom = -screenHeight/8
        }
        myPickerView = UIPickerView(frame: CGRect(x: x, y: y, width: width, height: height))
        
        bottom = self.view.layoutMarginsGuide.bottomAnchor
        trailing = self.view.layoutMarginsGuide.trailingAnchor
        
        myPickerView.self.delegate = self as UIPickerViewDelegate
        myPickerView.self.dataSource = self as UIPickerViewDataSource
        
        self.view.addSubview(myPickerView)
        
        
        if constraints {
            myPickerView.bottomAnchor.constraint(equalTo: bottom, constant:AnchorBottom).isActive = constraints
            myPickerView.trailingAnchor.constraint(equalTo: trailing, constant: AnchorTrailing).isActive = constraints
            myPickerView.widthAnchor.constraint(equalToConstant: w).isActive = constraints
            myPickerView.heightAnchor.constraint(equalToConstant: h).isActive = constraints
        }
        myPickerView.backgroundColor = UIColor.brown
        myPickerView.translatesAutoresizingMaskIntoConstraints = false
        myPickerView.target(forAction: #selector(pickerAction), withSender: myPickerView )
        //print(myPickerView.frame)
        if !old {
            let image = UIImage(named: "Breplace50.png")!

            execBut = UIButton(frame: CGRect(x: 20, y: screenHeight-50, width: w, height: h))
            execBut.setImage(image, for:UIControl.State.normal)
            //execBut = UIButton()
            execBut?.setTitle("", for: UIControl.State.normal)
            execBut?.tag = butType.regexp.rawValue
            execBut?.addTarget(self, action: #selector(buttonNBAction), for: .touchUpInside)

            self.view.addSubview(execBut!)
            //execBut.bottomAnchor.constraint(equalTo: bottom, constant:AnchorBottom).isActive = true
            print (myPickerView.frame)
            execBut.centerYAnchor.constraint(equalTo: myPickerView.centerYAnchor, constant: 0).isActive = constraints
            execBut!.trailingAnchor.constraint(equalTo: myPickerView.layoutMarginsGuide.leadingAnchor, constant: 0).isActive = constraints
            //print(execBut!.frame)
        }
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
