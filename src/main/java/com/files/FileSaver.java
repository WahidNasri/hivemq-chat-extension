package com.files;

import com.db.MyBatis;
import com.models.ChatMessage;
import com.models.MessageOriginality;
import com.models.MessageType;
import com.utils.JsonParser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

public class FileSaver {
    static String myAddress = "http://172.16.14.99/";
    public static ChatMessage saveAndGetMessageObject(String base64File, String room, String userId){
        String id = UUID.randomUUID().toString();

        String fileName = "File";
        try {
            String partSeparator = ",";
            //Example: data:image/png;base64,<encoded content>,<filename>
            if(base64File.contains(partSeparator)){
                String[] parts = base64File.split(partSeparator);
                String encodedImage = parts[1];
                String mime = parts[0].split(";")[0].split(":")[1];
                byte[] decodedImage = Base64.getDecoder().decode(encodedImage.getBytes(StandardCharsets.UTF_8));

                fileName = parts.length >= 3 ? parts[2] : "File";

                File fldr = new File("C:/xampp/htdocs/dashboard/");
                if(!fldr.exists()){
                    fldr.mkdirs();
                }
                File dest = new File(fldr, id + "." + getExtensionFromMime(mime));
                dest.createNewFile();
                String url = null;
                try (OutputStream stream = new FileOutputStream(dest.getPath())) {
                    stream.write(decodedImage);
                    url = myAddress + "dashboard/" + dest.getName();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String prevUrl = null;

                /*
                if(mime.contains("pdf")) {
                    File preview = PdfUtils.generatePdfPreview(dest);
                    if(preview != null){
                        prevUrl = myAddress + "dashboard/" + preview.getName();
                    }
                }
                //*/
                ChatMessage msg = new ChatMessage();
                msg.setId(id);
                msg.setFromId(userId);
                msg.setType(getMsgTypeFromMime(mime));
                msg.setAttachment(url);
                msg.setThumbnail(prevUrl);
                msg.setRoomId(room);
                msg.setOriginality(MessageOriginality.Original);
                msg.setText(fileName);
                msg.setSize(dest.length());

                msg.setMime(mime);

                return msg;
            }

        }catch (Exception ee){
            ee.printStackTrace();
        }
        return null;
    }
    static MessageType getMsgTypeFromMime(String mime){
        mime = mime.toLowerCase();
        if(mime.contains("image/")){
            return MessageType.ChatImage;
        }
        if(mime.contains("video/")){
            return MessageType.ChatVideo;
        }
        else if(mime.equals("application/pdf")
                || mime.equals("application/msword")
                || mime.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")){
            return MessageType.ChatDocument;
        }
        else if(mime.contains("audio/")){
            return MessageType.ChatAudio;
        }
        else return MessageType.ChatDocument ;
    }
    static String getExtensionFromMime(String mime){
        mime = mime.toLowerCase();
        if(mime.contains("image/")){
            return mime.split("/")[1];
        }
        if(mime.contains("video/")){
            return mime.split("/")[1];
        }
        else if(mime.equals("application/pdf")){
            return mime.split("/")[1];
        }
        else if(mime.equals("application/msword")){
            return "doc";
        }
        else if(mime.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")){
            return "odt";
        }
        else if(mime.contains("audio/")){
            return mime.split("/")[1];
        }
        else return "txt";//fixme: get the right extension
    }
}
