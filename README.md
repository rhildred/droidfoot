# droidfoot

This is a project by Dr. Dietrich Boles to allow greenfoot scenarios to run on Android. I made some minor changes for the project to run on Android Studio and with Dr. Boles kind permission am publishing my changes on github.

![Space Invaders](https://rhildred.github.io/courses/MB215/SpaceInvaders.png "Space Invaders")

In this article, I am getting [this space invaders clone](http://www.greenfoot.org/scenarios/15902) to run on Android. Getting a Greenfoot scenario onto Android has 3 basic parts:

##In Greenfoot

<iframe width="560" height="315" src="https://www.youtube.com/embed/hbI0Irwx59o" frameborder="0" allowfullscreen></iframe>

1) While still in Greenfoot, change your java world file to use setBackground("cell.jpg"); in the constructor to include its background image. This might also be a good time to make the size of your world to fit a phone:

```
public Space()
{    
    // Create a new world with 240x400 cells with a cell size of 1x1 pixels.
    super(240, 400, 1); 
    prepare();
    setBackground("background.jpg");
}

```
2) Also, still within Greenfoot, change your java actor files to use setImage("leaf.gif"); in the constructor to set the actor's image.

```
public class Player extends Actor
{
    public Player(){
        setImage("spaceship-icon.png");
    }

```
3) Test, to make sure that the world and actor classes still have images. (This is necessary,
because only the java, image and sound files are going to be used in the droidfoot Android
version of the project.)

4) Change the UI to be mouse based, rather than keyboard based:

```
public void act() 
{
    if(Greenfoot.isKeyDown("right")){
        move(4);
    }
    if(Greenfoot.isKeyDown("left"))
    {
        move(-4);
    }
    if (shotTimer >0){
        shotTimer = shotTimer -2;

    } else if(Greenfoot.isKeyDown("Space"))
    {
        getWorld().addObject(new Ordnance(this),getX(),getY());
        Greenfoot.playSound("pew_backup.wav");
        shotTimer = 68;
    }
}
```

In my case, I substituted clicking on the ship for the space bar (shooting) and the ship moving to where you touch for the arrow keys.

```

public void act() 
{
    if(Greenfoot.mousePressed(this)){
        getWorld().addObject(new Ordnance(this),getX(),getY());
        Greenfoot.playSound("pew_backup.wav");
        shotTimer = 68;
    }else if(Greenfoot.mousePressed(null)){
        MouseInfo info = Greenfoot.getMouseInfo();
        setLocation(info.getX(), getY());
    }
}

``` 

5) Test your scenario to make sure that it is mouse driven.

##In Android Studio

<iframe width="560" height="315" src="https://www.youtube.com/embed/tbWY_tOFpfY" frameborder="0" allowfullscreen></iframe>

1) Copy-paste the eclipse android project DFTemplate to a new eclipse project called Space Invaders (in this example).
2) Open Android studio and import the new eclipse project (SpaceInvaders in this example) in to a new Android Studio Project. Note* You may need to edit dFTemplate/build.gradle and droidfoot/build.gradle to have the latest compileSDKVersion
```
android {
    compileSdkVersion 23
    buildToolsVersion "23.0.2"

```
3) Open the file strings.xml in folder DFTemplate/src/main/res/values and replace the name DFTemplate by the name of your scenario (e.g. Space Invaders).
4) Rename the package org.droidfoot.dftemplate in folder src (e.g. org.droidfoot.spaceinvaders) (>Refactor > Rename).
5) Rename the file/class DFTemplateActivity in folder src in the package being renamed in step 4 (bspw. SpaceInvadersActivity)
6) Open the file AndroidManifest.xml and rename the package-attribute (second line) corresponding to the renaming in step 4 (e.g. package=“org.droidfoot.spaceinvaders“ )
7) Copy the java-files of your Greenfoot scenario into the package being renamed in step 4. Please note that you will have to edit all java-files to define the corresponding package-statement as the first statement (`package org.droidfoot.spaceinvaders;`).
8) Eliminate all errors in your java-files which can occur due to the differences between the original and the Android Greenfoot class library (see chapter 2).
9) Open the activity file being renamed in step 5, remove the comment in front the statement in method onCreate and assign the class object of your world class to the variable
DroidfootActivity.worldClass (DroidfootActivity.worldClass = SpaceInvadersWorld.class;
10) Copy the folders images and sounds which contain the images and sounds of your Greenfoot scenario into the folder DFTemplate/src/main/assets. If you have done this correctly, you will see the assets in android studio.

##Making the package

<iframe width="560" height="315" src="https://www.youtube.com/embed/H5DNgH8O1Wg" frameborder="0" allowfullscreen></iframe>

Finally you will make the Android package. 

15. Make a signed apk by going to the build menu in Android Studio.
16. Upload the apk, using google drive, and download it on to your phone.

These games may look a little bit crude, but with a little imagination, they can still be very fun.
