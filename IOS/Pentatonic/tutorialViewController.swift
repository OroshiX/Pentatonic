//
//  tutorialViewController.swift
//  Pentatonic
//
//  Created by Armand Hornik on 16/02/2018.
//  Copyright © 2018 Armand Hornik. All rights reserved.
//

import UIKit

class tutorialViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        drawPenta(image: myImageView, penta: tempPenta!, sizeSquare: myImageView.bounds.size.height)
    }

    @IBOutlet var myImageView: UIImageView!

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
    

    func drawPenta(image:UIImageView, penta:APenta, sizeSquare:CGFloat) {
        let width:Int = penta.width!
        let height:Int = penta.height!
        
        
        let divSize = CGFloat(max(width,height))
        let sizeBut = sizeSquare / divSize
        
        let sizeFont = CGFloat(sizeBut) * 0.8
        
        labelTuto.text = penta.name
        
        
        let renderer = UIGraphicsImageRenderer(size: CGSize(width: sizeSquare, height: sizeSquare))
        
        let img = renderer.image { ctx in
            
            let color = UIColor(rgb: 0xcccccc)
            
            ctx.cgContext.setFillColor(color.cgColor)
            ctx.cgContext.fill(CGRect(x: 0, y: 0, width: sizeSquare, height: sizeSquare))
            
            for i in 0..<height {
                for j in 0..<width {
                    let x=sizeBut/2+CGFloat(j)*sizeBut
                    let y=CGFloat(i)*sizeBut
                    let itsNeighbour = get_neighbour(penta:penta,i,j)
                    print ("i=\(i),j=\(j) - val=\(penta.data![i][j]) - x=\(x),y=\(y)")
                    drawRect(ctx.cgContext, CGFloat(x), CGFloat(y), CGFloat(sizeBut), CGFloat(sizeBut), itsNeighbour.up, itsNeighbour.down, itsNeighbour.right, itsNeighbour.left)
                    
                }
            }
            var atts = [NSAttributedStringKey.font: UIFont.systemFont(ofSize: sizeFont),NSAttributedStringKey.foregroundColor:UIColor.black]
            
            for arrayValeur in penta.valeurs! {
                
                let x = sizeBut*0.8 + CGFloat(arrayValeur.j!)*sizeBut
                let y =  CGFloat(arrayValeur.i!)*sizeBut
                
                ("\(arrayValeur.val!)" as NSString).draw(at: CGPoint(x: x, y: y), withAttributes: atts)
            }
        }
        image.image = img
    }
    
    
    @IBOutlet var labelTuto: UILabel!
    @IBAction func testActionPrimary(_ sender: Any, forEvent event: UIEvent) {
        print (String(describing: event))
        
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
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
