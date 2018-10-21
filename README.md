# dEMDR-c

## From the Wiki

### Integral Concepts

In order to understand this project, you need to be familiar with **EMDR**
therapy, which is used with remarkable effectiveness in treating **PTSD**.  
The acronym stands for _Eye Movement Desensitization and Reprocessing_.

  * [What is EMDR?](http://www.emdr.com/what-is-emdr/); from www.EMDR.com
  * [What is the EMDR session actually like?](https://emdria.site-ym.com/page/120); via EMDRia.site-ym.com
  * [dEMDR-c wiki home](https://gitlab.com/dgetsman/dEMDR-c/wikis/home)

### Notes Regarding Current Development

#### Reconsidering after recent 'emergency EMDR' needs

**NOTE**: This subsection is not yet included in the wiki.

_It should be noted that this reconsideration is subsequent to the below
decision to halt development._

I have had issues in the employment sector of my life for quite awhile now.
Recently I obtained a new job, after quite a stretch of unemployment.
Unfortunately, after being on the job for less than 10 days, the combination
of three separate, highly stressful, incidents ended up finally triggering a
PTSD fight/flight response association with my place of work.

I ended up having to leave work after the 3rd of the events, but this was
easily justifiable due to the fact that the incident involved physical injury,
and thus potential liability for the company if they didn't make some
concessions.

It didn't even take until work rolled around the next day to realize that
there was an association happening that would make it impossible for me to
function in my place of work.  I began calling to see if I could get in early
with my therapist, but being put on the 'call if cancellations occur' list was
the best that they could do.

Out of necessity, I decided to break out this app.  I'd since learned that you
don't need all of the sensory stims at play, and if that was the case, then
using the functional visual display (conveniently set to stop the session
after 5 minutes, by default) should provide some measure of being able to
defuse that reaction.

_It helped for me.  I'm still using it._

It occurred to me that, due to the state of mental health care in _this_
country, if not others, it might be the humanitarian thing to do to make a
usable version of this program available as an open alpha to those who really
need a session, especially if it's a situation like mine, where the trigger
had to be defused at risk of homelessness, inability to pay for necessities,
going hungry, etc.  I've been in those situations so many times from things
like this, and I feel that there is probably a large, and usually disregarded,
subset of people with financial and stability issues that could benefit from
such 'self-therapy' being available.  If you're in this category, I wish you
the best of luck, and _please let me know if there's anything that I can do to
help_.

So that brings us to this point (21oct18).  I'm going to disclaim the hell out
of this in documentation, put it under a development license, and throw that
into a release.  Apropos of it all: **use at your own risk**, and _hopefully_
your own benefit, as well.

#### After restarting EMDR under licenced therapy

After restarting EMDR under licensed oversight, again, I've become aware of a point in the official training that I was not previously aware of.  It seems that during the classes for certification, those training are advised to never try any sort of therapy on themselves with this technique due to a potential for getting stuck.

This seemed pretty unlikely to me, and I dismissed it as probably being a gimmick, ensuring that the copyrighted/trademarked therapy process keeps sending money back home, or at least copyright/trademark holders.  However, I was researching hypnotic states a bit the other night, and have decided to shelve this project, until I've been able to do the research on what kinds of brainwave changes it may induce while undergoing therapy.

If, for instance, it does induce some sort of hypnotic state, or anything based on REM/hypnogogia, this could actually be a legitimate concern.  I certainly don't want my project here doing any harm.  If anybody would care to help out with this research a bit, please let me know; I've not got enough time to go around right now, and it'd be greatly appreciated.

## Previous Development Ramble

I had originally started on a Java project to experiment with EMDR technology
not long ago.  I'd attempted to use a Slider object, however, and I guess this
widget is too complex to, or just decides not to, update on the display if the
value is changed more than every ~20-25ms or so.  Unfortunately, this makes it
unviable for the variable rate 'Knight Rider/Kitt' display that we need for
this therapy to work.

This project is implemented using javafx's Canvas objects, and seems to be
much more responsive, as well as allowing much more flexibility in display
options.  I don't think it's going to take much to get this project somewhat
useful, though adding features such as the tactile stim devices (especially if
utilizing _bluetooth_ connections) will provide added challenge, at some
point.

### Can't let the for-profit mental health professionals hold the keys of the kingdom forever, can we?


