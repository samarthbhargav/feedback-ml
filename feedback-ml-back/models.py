from schematics.models import Model
from schematics.types import StringType, BaseType
from schematics.types.compound import DictType
from schematics.exceptions import ValidationError
from common import  chk_type

class Document(Model):
    ID = StringType(required=True)
    content = DictType(StringType, required=True)

    def validate(self, partial=False, strict=False):
        super(Document, self).validate(partial, strict)
        if len(self.content) == 0:
            raise ValidationError("content cannot be empty")

    def __str__(self):
        return str(self.to_primitive())


class DocumentType(BaseType):
    def validate_document(self, value):
        value.validate()

class Record(Model):
    document =  DocumentType(required=True)
    label = StringType()

    def to_primitive(self):
        prim = {}
        prim["document"] = self.document.to_primitive()
        prim["label"] = self.label
        return prim

    def __str__(self):
        return "Record({}, {})".format(str(self.document), self.label)

if __name__ == "__main__":
    d = Document({"ID": "asd", "content" :  {"": ""}})
    print r.to_primitive()
    print r
    r.validate(strict=True)
