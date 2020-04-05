package be.coiba.fixedlength

final case class FieldDefinition(name: String, length: Int)

final case class RecordDefinition(fields: List[FieldDefinition])
