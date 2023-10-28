import React from 'react';
import { NavLink } from 'react-router-dom';

type Props = {
  icon: string;
  pageName: string;
  route: string;
  onClick: () => void;
};

const DrawerMenuItem: React.FC<Props> = ({
  icon,
  pageName,
  route,
  onClick,
}) => {
  const activeClass =
    'flex h-16 w-full items-center gap-y-2 bg-primaryColorHover px-4';
  const inactiveClass =
    'flex h-16 w-full items-center gap-xy2 px-4 hover:bg-primaryColorHover';

  return (
    <NavLink
      to={route}
      className={({ isActive }) => (isActive ? activeClass : inactiveClass)}
      onClick={() => onClick()}>
      <div className="flex h-full w-full items-center justify-start gap-x-4">
        <img
          src={icon}
          alt="page icon"
        />
        <span className="text-md cursor-pointer text-center font-semibold leading-tight text-textColor">
          {pageName}
        </span>
      </div>
    </NavLink>
  );
};

export default DrawerMenuItem;
