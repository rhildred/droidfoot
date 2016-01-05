package org.droidfoot.dfsnake;
import greenfoot.Actor;
import greenfoot.GreenfootImage;
import greenfoot.UserInfo;
import greenfoot.awt.Color;

import java.util.ArrayList;
import java.util.List;
// (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * This class easily creates a new highscore. see
 * http://www.greenfoot.org/scenarios/8385
 * 
 * @author (Busch2207)
 * @version (17.05.2013)
 * 
 * @author Dietrich Boles (some modifications)
 * @version 01.10.2013
 */
public class Highscore extends Actor {
    private int height, width, fieldNumber;
    private List<HighScoreField> listHSF = new ArrayList<HighScoreField>();
    private Color colorBackground, colorFrame;

    /**
     * Creates a new Highscore with the given colors. The order of the Colors
     * is: Highscore-Background / Highscore-Frame / String-Color / Color of the
     * highlighted Score-Field-Frame / Color of the highlighted Score-Field /
     * Color of the general Score-Field-Frame / Color of the general Score-Field
     */
    public Highscore(List<UserInfo> userinfoList, int width, int height,
            int listFieldNumber, UserInfo uiMyData, Color colorBackground,
            Color colorFrame, Color colorString, Color colorMyScoreFrame,
            Color colorMyScoreField, Color colorGeneralScoreFrame,
            Color colorGeneralScoreField,
            Color colorBackgroundScoreField) {
        this.fieldNumber = listFieldNumber;
        this.colorBackground = colorBackground;
        this.colorFrame = colorFrame;
        this.height = height;
        this.width = width;
        if (userinfoList != null) {
            int iCount = 0;
            for (UserInfo pd : userinfoList) {
                if (pd != null) {
                    listHSF.add(new HighScoreField(pd, uiMyData, width - 2,
                        (height - 2) / listFieldNumber, colorString,
                        colorMyScoreFrame, colorMyScoreField,
                        colorGeneralScoreFrame, colorGeneralScoreField, colorBackgroundScoreField));
                } else {
                    listHSF.add(new HighScoreField(width - 2,
                        (height - 2) / listFieldNumber, colorString,
                        colorMyScoreFrame, colorMyScoreField,
                        colorGeneralScoreFrame, colorGeneralScoreField, colorBackgroundScoreField));  
                }
                if (++iCount >= fieldNumber)
                    break;
            }
        }
        updateImage();
    }

    private void updateImage() {
        GreenfootImage i = new GreenfootImage(width, height);
        i.setColor(colorBackground);
        i.fillRect(1, 1, width - 2, ((height - 2) / fieldNumber) * fieldNumber);
        i.setColor(colorFrame);
        i.drawRect(0, 0, width - 1, height - 1);
        i.fillRect(1, ((height - 2) / fieldNumber) * fieldNumber + 1,
                width - 2, height - ((height - 2) / fieldNumber) * fieldNumber
                        - 2);

        int y = 1;
        for (HighScoreField HSF : listHSF) {
            GreenfootImage i2 = HSF.getImage();
            i.drawImage(i2, 1, y);
            y += i2.getHeight();
        }
        setImage(i);
    }

    private static Color colorInvisible = new Color(0, 0, 0, 0);

    @SuppressWarnings("unused")
    private static GreenfootImage scaleToMax(GreenfootImage giImage, int width) {
        return scaleToMax(giImage, width, width);
    }

    private static GreenfootImage scaleToMax(GreenfootImage giImage, int width,
            int height) {
        giImage = new GreenfootImage(giImage);
        int w = giImage.getWidth(), h = giImage.getHeight();
        if (w < width && h < height) {
            return giImage;
        }
        double precent;
        if (w > h)
            precent = (double) width / (double) w;
        else
            precent = (double) height / (double) h;
        giImage.scale((int) (precent * w), (int) (precent * h));
        return giImage;
    }

    private static GreenfootImage getTextMaxWidth(int width, String S,
            int startsize, Color colorString) {
        return getTextMaxWidth(width, S, startsize, colorString, colorInvisible);
    }

    private static GreenfootImage getTextMaxWidth(int width, String S,
            int startsize, Color colorString, Color colorBackground) {
        for (; startsize > 0; startsize--) {
            GreenfootImage i = new GreenfootImage(S, startsize, colorString,
                    colorBackground);
            if (i.getWidth() <= width)
                return i;
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
	public static List<UserInfo> merge(List<UserInfo> topList, List<UserInfo> nearbyList, int number) {
        topList = UserInfo.getTop(number);
        UserInfo currentUser = UserInfo.getMyInfo();
        if (currentUser == null) {
            return topList;
        }
        if (topList.size() == 0) {
            return nearbyList;
        }
        if (contains(topList, currentUser)) {
            return topList;
        }
        if (!contains(nearbyList, currentUser)) {
            return topList;
        }
        List<UserInfo> list = new ArrayList<UserInfo>();
        int to = Math.min(topList.size(), number-2);
        for (int i=0; i<to; i++) {
            list.add(topList.get(i));
        }
        if (currentUser.getRank() != list.size() + 1) {
            list.add(null);
        }
        list.add(currentUser);
        
        int ci = getCurrentIndex(nearbyList);
        if (list.size() < number) {
          if (ci > 0) {
              UserInfo ci1 = nearbyList.get(ci-1);
              if (ci1.getRank() == list.size() - 1) {
                  list.remove(null);
              }
              if (!contains(list, ci1)) {
                  list.add(list.size()-1, ci1);
              }

            }
        }
       
        int j= ci + 1;
        for (int i = list.size(); i<number; i++) {
            if (j < nearbyList.size()) {
                list.add(nearbyList.get(j));
                j++;
            } else {
                break;
            }
        }
        return list;
    }
    
    private static boolean contains(List<UserInfo> list, UserInfo user) {
        for (UserInfo ui : list) {
            if (ui == null) continue;
            if (ui.getUserName().equals(user.getUserName())) {
                return true;
            }
        }
        return false;
    }
    
    private static int getCurrentIndex(List<UserInfo> list) {
        UserInfo currentUser = UserInfo.getMyInfo();
        for (int i=0; i<list.size(); i++) {
            if (list.get(i) == null) continue;
            if (list.get(i).getUserName().equals(currentUser.getUserName())) {
                return i;
            }
        }
        return -1;
    }

    private static GreenfootImage giField;
    private static int iHsfHeight = 3, iHsfWidth = 3;
    private static boolean lastEqual = false;

    private class HighScoreField {
        private Color col_String, col_MyScoreFrame, col_MyScore,
                col_ScoreFrame, col_Score, col_BackScoreField;
        private UserInfo pdData, pdMyData;
        private GreenfootImage giImage;

        public HighScoreField(UserInfo pdData, UserInfo pdMyData,
                int i_hsf_width, int i_hsf_height, Color col_String,
                Color col_MyScoreFrame, Color col_MyScore,
                Color col_ScoreFrame, Color col_Score,
                Color colorBackgroundScoreField) {
            this.col_String = col_String;
            this.col_MyScoreFrame = col_MyScoreFrame;
            this.col_MyScore = col_MyScore;
            this.col_ScoreFrame = col_ScoreFrame;
            this.col_Score = col_Score;
            this.col_BackScoreField = colorBackgroundScoreField;
            this.pdData = pdData;
            this.pdMyData = pdMyData;
            scale(i_hsf_height, i_hsf_width);
            updateImage();
        }
        
        public HighScoreField(int i_hsf_width, int i_hsf_height, Color col_String,
                Color col_MyScoreFrame, Color col_MyScore,
                Color col_ScoreFrame, Color col_Score,
                Color colorBackgroundScoreField) {
            this.col_String = col_String;
            this.col_MyScoreFrame = col_MyScoreFrame;
            this.col_MyScore = col_MyScore;
            this.col_ScoreFrame = col_ScoreFrame;
            this.col_Score = col_Score;
            this.col_BackScoreField = colorBackgroundScoreField;
            this.pdData = null;
            this.pdMyData = null;
            scale(i_hsf_height, i_hsf_width);
            updateImageEmpty();
        }

        private void scale(int i_i_hsf_height, int i_i_hsf_width) {
            if (iHsfHeight == i_i_hsf_height
                    && iHsfWidth == i_i_hsf_width
                    && lastEqual == (pdMyData != null && pdData.getUserName()
                            .equals(pdMyData.getUserName())))
                return;
            lastEqual = (pdMyData != null && pdData.getUserName().equals(
                    pdMyData.getUserName()));
            iHsfHeight = i_i_hsf_height;
            iHsfWidth = i_i_hsf_width;
            giField = new GreenfootImage(iHsfWidth, iHsfHeight);
            giField.setColor((pdMyData != null
                    && pdData.getUserName().equals(pdMyData.getUserName()) ? col_MyScoreFrame
                    : col_ScoreFrame));
            giField.drawRect(0, 0, iHsfWidth - 1, iHsfHeight - 1);
            giField.setColor((pdMyData != null
                    && pdData.getUserName().equals(pdMyData.getUserName()) ? col_MyScore
                    : col_Score));
            giField.fillRect(1, 1, iHsfWidth - 2, iHsfHeight - 2);
            giField.setColor(col_BackScoreField);
            giField.fillRect(iHsfWidth  * 4 / 5, 1, iHsfWidth * 1 / 5,
                    iHsfHeight - 2);
        }

        public void updateImage() {
            GreenfootImage i = new GreenfootImage(giField), gi_Rank = getTextMaxWidth(
                    iHsfWidth * 1 / 10 - 2, "" + pdData.getRank(),
                    (int) (iHsfHeight * 0.75), col_String), gi_UserName = getTextMaxWidth(
                    (int) (iHsfWidth * 0.45), pdData.getUserName(),
                    iHsfHeight * 2 / 3, col_String), gi_UserScore = getTextMaxWidth(
                    iHsfWidth * 2 / 10 - 2, pdData.getScore() + "",
                    iHsfHeight * 2 / 3, col_String);
            // GreenfootImage gi_UserImage = scaleToMax(pdData.getUserImage(),
            // iHsfHeight - 2); dibo
            i.drawImage(gi_Rank, iHsfWidth * 1 / 20 - gi_Rank.getWidth() / 2,
                    iHsfHeight / 2 - gi_Rank.getHeight() / 2);
            // i.drawImage(gi_UserImage, iHsfWidth * 1 / 10, iHsfHeight / 2
            // - gi_UserImage.getHeight() / 2); dibo
            i.drawImage(gi_UserName,
                    (iHsfWidth * 7 / 10 - 0 + 2) / 2
                            + iHsfWidth * 1 / 10 + 0
                            - gi_UserName.getWidth() / 2, iHsfHeight / 2
                            - gi_UserName.getHeight() / 2);
            i.drawImage(gi_UserScore,
                    iHsfWidth * 9 / 10 - gi_UserScore.getWidth() / 2 + 1,
                    iHsfHeight / 2 - gi_UserScore.getHeight() / 2);
            this.giImage = i;
        }
        
        public void updateImageEmpty() {
            GreenfootImage i = new GreenfootImage(giField), gi_Rank = getTextMaxWidth(
                    iHsfWidth * 1 / 10 - 2, "" + "",
                    (int) (iHsfHeight * 0.75), col_String), gi_UserName = getTextMaxWidth(
                    (int) (iHsfWidth * 0.45), "...",
                    iHsfHeight * 2 / 3, col_String), gi_UserScore = getTextMaxWidth(
                    iHsfWidth * 2 / 10 - 2, "" + "",
                    iHsfHeight * 2 / 3, col_String);
            // GreenfootImage gi_UserImage = scaleToMax(pdData.getUserImage(),
            // iHsfHeight - 2); dibo
            i.drawImage(gi_Rank, iHsfWidth * 1 / 20 - gi_Rank.getWidth() / 2,
                    iHsfHeight / 2 - gi_Rank.getHeight() / 2);
            // i.drawImage(gi_UserImage, iHsfWidth * 1 / 10, iHsfHeight / 2
            // - gi_UserImage.getHeight() / 2); dibo
            i.drawImage(gi_UserName,
                    (iHsfWidth * 7 / 10 - 0 + 2) / 2
                            + iHsfWidth * 1 / 10 + 0
                            - gi_UserName.getWidth() / 2, iHsfHeight / 2
                            - gi_UserName.getHeight() / 2);
            i.drawImage(gi_UserScore,
                    iHsfWidth * 9 / 10 - gi_UserScore.getWidth() / 2 + 1,
                    iHsfHeight / 2 - gi_UserScore.getHeight() / 2);
            this.giImage = i;
        }

        public GreenfootImage getImage() {
            return new GreenfootImage(giImage);
        }
    }
}

