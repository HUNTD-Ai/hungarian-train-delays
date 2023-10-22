import React from 'react';
import { NavLink } from 'react-router-dom';

type Props = {
  icon: string;
  pageName: string;
  route: string;
};

const NavbarItem: React.FC<Props> = ({ icon, pageName, route }) => {
  const activeClass =
    'flex h-full items-center gap-x-2 bg-primaryColorHover px-4';
  const inactiveClass =
    'flex h-full items-center gap-x-2 px-4 hover:bg-primaryColorHover';

  return (
    <NavLink
      to={route}
      className={({ isActive }) => (isActive ? activeClass : inactiveClass)}>
      <div className="flex items-center gap-x-2">
        <img
          src={icon}
          alt="page icon"
        />
        <span className="cursor-pointer text-lg font-semibold text-textColor">
          {pageName}
        </span>
      </div>
    </NavLink>
  );
};

export default NavbarItem;
