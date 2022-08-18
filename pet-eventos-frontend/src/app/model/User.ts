import { Group } from './Group';

export class User {
  idUser: number;
  email: string;
  password: string;
  name: string;
  registration: string;
  userType: string;
  isPresent: boolean;
  group: Group = new Group();
}
