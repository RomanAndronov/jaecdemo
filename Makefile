MF = /tmp/aecDemoManifest

AECDEMO = aecdemo.jar
SRCDIR = aecdemo

JFLAGS = -g
JAVAC = javac -cp ./$(SRCDIR):${CLASSPATH}

.SUFFIXES: .java .class
.java.class:
	$(JAVAC) $(JFLAGS) $<

_AECDEMO_SRC = AecDemoFrame.java \
	AecDemoApplet.java \
	AecDemoPanel.java \
	AecDemoItem.java \
	AecDemoLabel.java \
	AecDemoButton.java \
	AecDemoUi.java \
	AeNotation.java \
	Prefix.java \
	Postfix.java

AECDEMO_SRC = $(_AECDEMO_SRC:%=$(SRCDIR)/%)

AECDEMO_CLASSES = $(AECDEMO_SRC:.java=.class)

$(AECDEMO): $(AECDEMO_SRC) $(AECDEMO_CLASSES)
	rm -f $(MF)
	echo "Main-Class: $(SRCDIR)/AecDemoFrame" > $(MF)
	jar cmf $(MF) $@ $(SRCDIR)/*.class
	rm -f $(MF)

clean:
	rm -f $(AECDEMO) $(SRCDIR)/*.class
