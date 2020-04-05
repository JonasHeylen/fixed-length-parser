package be.coiba.fixedlength

object StringParser {
  def parse(input: String, definition: RecordDefinition): Record = {
    Record(
      definition.fields
        .foldLeft(ParserState()) {
          case (parserState, fieldDefinition) =>
            val (fieldValue, remainder) = input.splitAt(fieldDefinition.length)
            parserState.update(Field(fieldDefinition.name, fieldValue), remainder)
        }
        .fields
    )
  }

  final case class ParserState(_fields: List[Field] = List.empty, remainder: String = "") {
    def update(newField: Field, newRemainder: String): ParserState =
      copy(newField :: fields, newRemainder)
    def fields: List[Field] = _fields.reverse
  }
}
