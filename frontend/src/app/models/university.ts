export class University {
    id: number;
    name: string;
    abbreviation: string;
    faculties: Array<{}>;

    public constructor(id?: number, name?: string, abbreviation?: string) {
      this.id = id;
      this.name = name;
      this.abbreviation = abbreviation;
    }

    public equals(other: any) {
      if (other == null || !(other instanceof University)) {
        return false;
      }

      return this.id == other.id &&
        this.name == other.name &&
        this.abbreviation == other.abbreviation;
    }
}
