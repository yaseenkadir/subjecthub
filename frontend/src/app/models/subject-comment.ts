export class SubjectComment {
    id: number;
    user: object;
    subject: object;
    post: string;
    postTime: Date; //not sure if this the right datatype
    isFlagged: boolean;
    thumbsUp: number;
    thumbsDown: number;
}
