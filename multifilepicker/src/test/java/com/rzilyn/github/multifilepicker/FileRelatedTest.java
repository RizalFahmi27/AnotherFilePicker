package com.rzilyn.github.multifilepicker;

import com.rzilyn.github.multifilepicker.utils.Util;

import org.junit.Test;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Rizal Fahmi on 16-Dec-17.
 */

public class FileRelatedTest {

    @Test
    public void testFileSizeWithPrefix(){
        for(Number n : Arrays.asList(15,100,1546,11253,304445,421999432,421999432508L)){
            long l = n.longValue();
            System.out.println("Value : "+ Util.getFileSizeString(l));
        }
    }

    @Test
    public void testFileExt(){
        String actual[] = {"/example/path/to/file.pdf",
                "/example/path/file.aac",
                "/example/to/file.fadga",
                "/example/file.abc",
                "/example/dot.path/file.jpg",
                "/example/dot.path/dot.file.zip"
        };
       String expected[] = {"pdf","aac","fadga","abc","jpg","zip"};

       for(int i=0;i<actual.length;i++){
           assertEquals("Get fileType failed",expected[i],Util.getFileExtension(actual[i]));
       }
    }
}
