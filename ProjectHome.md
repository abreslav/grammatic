**Language Engineering** is an interdisciplinary area addressing many problems related to (mainly textual) computer languages. Parsing and translation are the most widely known ones, but there are many more: "static semantics" (typing and other static analyses), creating Integrated Development Environments (IDEs), which includes syntax highlighting, building outlines, code navigation, completion and refactoring, language documentation and dialect maintenance and many more. Many of such problems are solved in a generative fashion: we write high-level specifications (like grammars with embedded semantic actions for parsing) and generate code from them (like lexers, parsers and so on). Again, parsing is not the only domain: syntax highlighting, code navigation and completion, pretty-printing and many more problems may be (and are indeed) solved in this manner.

Now if we come up with a language (which is an everyday practice if you consider DSLs) we have to specify solutions for many problems for it. The problem is that specification languages are built upon a grammar of the language in question (consider parsing: we embed semantic actions into grammar rules, other languages work the same way) and if we specify many solutions separately, we have to duplicate the grammar (in many practical cases the grammar is not duplicated but changes slightly from one specification to another, with no intension from the developer, introducing inconsistencies in language-supporting tools). The only existing alternative is to attach all the specifications to the same grammar, thus putting them into the same file, which leads to huge unreadable specifications.

Grammatic offers a solution for this problem based on usage of a pointcut-advice language, inspired by AspectJ, to attach specifications to grammars. This language enables separation of concerns and reuse of specifications.

We successfully applied it to parser specifications, IDE specifications and grammar transformations.


On Grammarware engineering see
  * [P. Klint, R. Lämmel and C. Verhoef, Towards an engineering discipline for grammarware](http://citeseerx.ist.psu.edu/viewdoc/summary?doi=10.1.1.5.3403)

About Grammatic:
  * In English:
    * [Using Grammatical Aspects in Language Engineering (slides)](http://grammatic.googlecode.com/svn/papers/tallinn_Nov_09/tallinn_talk.pdf)
    * [Creating modular and reusable DSL textual syntax definitions with Grammatic/ANTLR](http://arxiv.org/abs/0902.2621)
    * [Grammatic -- a tool for grammar definition reuse and modularity](http://arxiv.org/abs/0901.2461)
    * [More papers/Slides](http://abreslav.googlepages.com)
  * In Russian
    * [Средства повторного использования формальных грамматик и их применение для создания диалектов](http://abreslav.googlepages.com/grammar_modularity_and_reuse_kmu_09.pdf)
    * [Применение принципов MDD и аспектно-ориентированного программирования к разработке ПО, связанного с формальными грамматиками](http://abreslav.googlecode.com/svn-history/r431/work/grammatic/KMU/BRESLAV_A_A.pdf)
    * [Генерация моделей, основанная на шаблонах: реализация при помощи обобщенных классов](http://abreslav.googlepages.com/templates.pdf)