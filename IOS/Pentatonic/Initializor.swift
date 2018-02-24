//
//  Initializor.swift
//  Pentatonic
//
//  Created by Armand Hornik on 15/02/2018.
//  Copyright © 2018 Armand Hornik. All rights reserved.
//

import Foundation

struct Initializor {
    let name: String
    let line: Int
    let column: Int
    var table :[[Int]] = [[Int]]()

    init(name: String, line:Int, column:Int){
        self.name = name
        self.line = line
        self.column = column
        
    }
    func createLevel() {
        let def:define = define.init()
        let paths = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)
        
        
        let documentsDirectory = paths[0]



        var mystr:String
        
        for level in def.allLevel
        {
            mystr = def.level[level]! 
            let dataPath = documentsDirectory.appending("/\(mystr)")
            do {
                try FileManager.default.createDirectory(atPath: dataPath, withIntermediateDirectories: false, attributes: nil)
            } catch let error as NSError {
                print(error.localizedDescription);
            }
            
        }
    }
    func secTest() ->String{
        var paths = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)
            var documentsDirectory : String;
            documentsDirectory = paths[0] as String
        let fileManager: FileManager = FileManager()
        var fileList: NSArray = []
        do {
            fileList = [try fileManager.contentsOfDirectory(atPath: documentsDirectory)]
        } catch {
            print("Error 1")
        }
        let filesStr: NSMutableString = NSMutableString(string: "Files in Documents folder \n")
        var u:[String] = ["toto"]
        for s in fileList {
            u = s as! [String]
            for val:String in u {
            filesStr.appendFormat("%@\n", val)
            }
        }
            
        return filesStr as String
    }
    
    func displayPenta(fileStr:String) {
        
        if let directory = FileManager.default.urls(for: .adminApplicationDirectory, in: .allDomainsMask).first {
            
            
                let fileURL = directory.appendingPathComponent(fileStr)
            
                //reading
                do {
                    let text2 = try String(contentsOf: fileURL, encoding: .utf8)
                    print(text2)
                    
                }
                catch {/* error handling here */
                    print ("Error Aller-Retour not existing")
                }
            
        }
    }
    func getPentaString (_ namePenta: String, directory: String) -> String {
        let path:String = Bundle.main.path(forResource: namePenta.components(separatedBy: ".").first, ofType: "penta")!
        let mystr = try! String(contentsOfFile: path, encoding: String.Encoding.utf8)
        return mystr
    }
    func listAllFiles () {
        
        
        for _ in 0..<2 {
        let path:String = Bundle.main.path(forResource: "logo", ofType: "penta")!
            
            
            var Directory:String = path
            var components = path.components(separatedBy: "/")
            if components.count > 1 {
                components.removeLast()
                Directory = components.joined(separator: "/")
            }
                print( "Directory = \(Directory)")
            
            
            let fileManager: FileManager = FileManager()
            var fileList: NSArray = []
            do {
                fileList = [try fileManager.contentsOfDirectory(atPath: Directory)]
            } catch {
                print("Error 1")
            }
            let filesStr: NSMutableString = NSMutableString(string: "Files in Documents folder \n")
            var u:[String] = ["toto"]
            for s in fileList {
                u = s as! [String]
                for val:String in u {
                    filesStr.appendFormat("%@\n", val)
                }
            }
            print (filesStr)
            
            
            let _:String
        // mystr = try! String(contentsOfFile: path, encoding: String.Encoding.utf8)
        // print (mystr)
        }
     
    }
    func listFiles (directory:String, suffix:String)->[String] {
        var arrayStrings:[String] = []
        
        let fileManager: FileManager = FileManager()
        var fileList: NSArray = []
        do {
            fileList = [try fileManager.contentsOfDirectory(atPath: directory)]
        } catch {
            print("Error 1")
        }
        var u:[String]
        for s in fileList {
            u = s as! [String]
            for val in u {
            if val.split(separator: ".").map(String.init).last == "penta"
            {
                arrayStrings.append(val)
            }
            
            }
        }
        print (arrayStrings)
        
        
        
        return arrayStrings
    }
}
