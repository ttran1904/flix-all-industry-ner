# NER-Model-Flix-Cars

Analyze Flix's webcrawled data through a NER (Name Entity Recognition) model for certain product's aspects: Brand, Product, Person, Aspect, Organization, and Location. All trained models are in Vietnamese with a few English.

The model basically partition sentences to words and establish the Vietnamese grammar structure, and then it identifies the noun/adverb/compound noun or adverbs to identify if they are the aspects identified above.

Since Vietnamese has complex compound nouns/adverbs, the model also have to determine which consecutive words would combine to make an appropriate compound noun/verb. I train the model on certain Tags, and here's some guildlines on how it works:
Format: /<B/I>-<ASPECT>
- B/I: B means the beginning of a compound noun/adverb. I means the training words (2nd up till the end)
- ASPECT: Can be either BRAND, LOC, PRODUCT, PER, ASPECT, ORG.

If it's not a significant noun or adverb, then it's "/O".

I use this model to analyze crawled data from blog post, facebook posts, websites, etc.
