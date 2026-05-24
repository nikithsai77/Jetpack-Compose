This is the Local AppSearch Database, It make's the search operation very efficiently and It's has Pagination as well.

1) Data class Annotated with @Document This Annotation Tells To The Annotation Processor To Generate a Required Code To Store in the AppSearch DB.
And Annotation Processor will scan the class and read the annotation and use the generated helper class Internally Like Serialization, Deserialization,
Schema, Indexing To Store In AppSearch DB with KSP, Kapt.
2) Without This @Document annotation on data class then Object will be Available Only in Memory and AppSearch doesn't know about it and ignore this 
instance to store in this AppSearch DB.

General ::
1) Document - actual searchable object in AppSearch Database. Simply Generated Code Convert This Kotlin/Java Object Into JSON Response in Internal Storage
for Searchable, that Json Response is called Document.
2) Document Storage - is used to store all the document's object's.

         Example: DOC1, DOC2 -> DOC Storage.
3) Schema Storage - is generate by appsearch by metadata file is used to find the which field's are searchable, field's are NameSpace, field's are ID.
                    based-upon it generates the Tokenization.
4) Tokenization / Tokenized Terms - The AppSearch break's the search text into piece's and created the Inverted Index.
        
          Example: DocId1 - Milk from supermarket.
                   DocId2 - Milk for Morning.
 
          Inverted Index : Milk - [DocId1, DocId2]
                           from - [DocId1]
                           supermarket - [DocId1]
                           for - [DocId2]
                           Morning - [DocId2]

Note : 1) Search Engine Don't Check The Search Text Into All Doc's with Searchable Field's It's Very Expansive, So That's Why 
          Search Engine Break's The Text And Check With Inverted Index and Get The DocId Value's From It and Fetch Those 
          Corresponding The DocId's from The Internal Storage And Return's It.
       2) So When The User Search The Text, The AppSearch Engine Fetches Those DocId's and Returns That How Search Becomes Efficiently.

          Kotlin Object -> Serialzation -> Doc -> Doc Storage -> Tokenzation -> Inverted Index

       Example : search("milk") -> Milk from supermarket. , Milk for Morning. (Happen's With Inverted Index by AppSearch Engine)
