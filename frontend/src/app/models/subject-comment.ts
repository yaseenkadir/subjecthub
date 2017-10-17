import { User } from './user';

export class SubjectComment {
    id: number;
    user: User;
    subject: object;
    post: string;
    postTime: Date;
    flagged: boolean;
    thumbsUp: number;
    thumbsDown: number;
}
