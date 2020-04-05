package be.coiba.fixedlength

final case class Field(name: String, value: String)

final case class Record(fields: List[Field]) {
    def maxFieldNameLength: Int = fields.map(_.name.length).max
}
