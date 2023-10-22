import React from 'react';
import { NavLink } from 'react-router-dom';

type Props = {
  icon: string;
  pageName: string;
  route: string;
};

const NavbarItem: React.FC<Props> = ({ icon, pageName, route }) => {
  const activeClass =
    'flex h-full w-full sm:w-auto items-center gap-x-2 bg-primaryColorHover px-4 py-2';
  const inactiveClass =
    'flex h-full w-full sm:w-auto items-center gap-x-2 px-4 py-2 hover:bg-primaryColorHover';

  return (
    <NavLink
      to={route}
      className={({ isActive }) => (isActive ? activeClass : inactiveClass)}>
      <div className="flex w-full flex-col items-center justify-center gap-x-2 sm:flex-row sm:justify-start">
        <img
          src={icon}
          alt="page icon"
        />
        <span className="text-md cursor-pointer text-center font-semibold leading-tight text-textColor sm:text-lg">
          {pageName}
        </span>
      </div>
    </NavLink>
  );
};

export default NavbarItem;
