export class Assessment {
  id: number;
  name: string;
  description: string;
  weighting: number;
  groupWork: boolean;
  length: string;
  type: AssessmentType;

  constructor(id?: number,
              name?: string,
              description?: string,
              weighting?: number,
              groupWork?: boolean,
              length?: string,
              type?: AssessmentType) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.weighting = weighting;
    this.groupWork = groupWork;
    this.length = length;
    this.type = type;
  }

  equals(other: any) {
    if (other == null || !(other instanceof Assessment)) {
      return false;
    }

    return this.id == other.id &&
      this.name == other.name &&
      this.description == other.description &&
      this.weighting == other.weighting &&
      this.groupWork == other.groupWork &&
      this.length == other.length &&
      this.type == other.type;
  }
}

// TODO: Upgrade to typescript 2.4 so we can use string enums
export enum AssessmentType {
  REPORT,
  TEST,
  FINAL,
  PROJECT
}

export function assessmentTypeFromString(s: string): AssessmentType {
  switch(s) {
    case 'PROJECT': return AssessmentType.PROJECT;
    case 'REPORT':  return AssessmentType.REPORT;
    case 'FINAL':   return AssessmentType.FINAL;
    case 'TEST':    return AssessmentType.TEST;
    default:
      throw new Error(`Unexpected assessment type: "${s}".`)
  }
}
