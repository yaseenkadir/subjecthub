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
}
