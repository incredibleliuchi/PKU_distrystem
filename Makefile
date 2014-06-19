EMPTY = 
SPACE = $(EMPTY) $(EMPTY)
CLASSPATH = $(subst $(SPACE),:,$(wildcard lib/*.jar))
JCFLAGS = -d bin -cp $(CLASSPATH) -sourcepath src -encoding utf8
JAVAC = javac

TEXT_TEMPLATE = "\033[36mTEXT\033[0m"
COMMA = ","

SOURCE_JAVA = $(shell find src -name *.java)
SOURCE_XML = $(shell find src -name *.xml)
CLASS_FILES = \
$(foreach file,$(SOURCE_JAVA),$(patsubst src%.java,bin%.class,$(file))) \
$(foreach file,$(SOURCE_XML),$(patsubst src%,bin%,$(file)))

all: bin $(CLASS_FILES)

bin:
	@echo $(subst TEXT,"Making directory $@",$(TEXT_TEMPLATE))
	mkdir -p bin

bin/%.class: src/%.java
	@echo $(subst TEXT,"Compiling $< to $@ ...",$(TEXT_TEMPLATE))
	$(JAVAC) $(JCFLAGS) $<

bin/%.xml: src/%.xml
	@echo $(subst TEXT,"Coping $< to $@ ...",$(TEXT_TEMPLATE))
	cp $< $@

clean:
	@echo $(subst TEXT,"Removing Class Files.",$(TEXT_TEMPLATE))
	$(RM) bin/* -r
	@echo $(subst TEXT,"Clean.",$(TEXT_TEMPLATE))

.PHONY: clean all
