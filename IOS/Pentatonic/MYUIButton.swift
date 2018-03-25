//
//  MYUIButton.swift
//  Pentatonic
//
//  Created by Armand Hornik on 23/03/2018.
//  Copyright © 2018 Armand Hornik. All rights reserved.
//

import UIKit

class MYUIButton: UIButton {

    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */
    override func point(inside point: CGPoint, with event: UIEvent?) -> Bool {
        let px = point.x
        let py = point.y
        let bx = super.frame.minX
        let by = super.frame.minY
        let wx = super.frame.width
        if tag == 8 {
        print ("\n*****\nMYUIB: \(point.debugDescription)")
        print ("MYUIB: \(self.debugDescription)")
        print ("MYUIB: \(super.debugDescription)")
        print ("MYUIB: \(superview.debugDescription)")
            let w = 320/3
            let h = 520/3
            
        }
        if super.frame.contains(point) { print ("contains ******* !!!!")}
        if (abs(Int(px - bx)) < Int(wx)) && (abs(Int(py - by)) < Int(wx)) {
        print ("\(self.tag)is activated")
        }
        
        
        
//        for subview in self.subviews as [UIView] {
//
//            if !subview.isHidden && subview.alpha > 0 && subview.isUserInteractionEnabled && subview.point(inside:point, with: event) {
//                return true
//            }
//        }
        
        return super.point(inside: point, with: event)
    }

}
