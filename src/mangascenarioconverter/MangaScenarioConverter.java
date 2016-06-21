/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mangascenarioconverter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author 瀬尾浩史
 */
public class MangaScenarioConverter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //　入力ファイル
        File inFile = new File(args[0]);
        System.out.println("入力ファイル: " + inFile);
        
        // 出力ファイル
        Path path = inFile.toPath();
        Path filename = path.getFileName();
        String newFilename = filename.toString().replace(".txt", "_変換済み.txt");
        File outFile = path.resolveSibling(newFilename).toFile();
        System.out.println("出力ファイル: " + outFile);

        //　改行文字の取得
        String crlf = System.getProperty("line.separator");
        
        //　各種正規表現
        Pattern pDialogue = Pattern.compile("^.+(\t| |　)+(「|（|『)(.+)(」|）|』)$");
        Pattern pSeparater = Pattern.compile("^_+$");
        
        
        BufferedReader br = null;
        BufferedWriter bw = null;
        
       
        try {
            // ファイルのオープン
            br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile),"UTF-8"));
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8"));
           
            // 一行ごとに読み込んで処理
            String line = null;
            while ((line = br.readLine()) != null) {
                
                String newline = null;
                Matcher m = null;
                
                // セパレーター行の抽出
                m = pSeparater.matcher(line);
                if (m.find()){
                    newline = m.group();
                }
                
                // セリフの抽出
                m = pDialogue.matcher(line);
                if (m.find()){
                    // 括弧に囲まれた部分を抽出
                    newline = m.group(3);
                    // 。か全角スペースがあったら改行x2
                    newline = newline.replaceAll("。|　| ",crlf+crlf);
                    // 、があったら改行x1
                    newline = newline.replaceAll("、",crlf);
                    
                }                 
 
                // 行が空なら次へ
                if (newline == null) { continue; }

                // バッファに書き込み
                bw.write(newline);
                bw.newLine();
                bw.newLine();
                
            }
            
            // ファイルに書き出し
            bw.flush();
            
        } catch (IOException e) {
            
            System.out.println("ファイル書き込みエラーです");
            
        } finally {
            
            try {
                if(br != null) br.close();
                if(bw != null) bw.close();
            } catch (IOException e2) {
            }
          
        }

        
        



        
    }
    
}
