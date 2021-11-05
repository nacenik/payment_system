CURRENT_USER=$(logname)

create_dir () {
  if [ ! -d "$MK_DIR" ]; then
    echo "Create dir ${MK_DIR}"
    mkdir -p $MK_DIR
  else
    echo "Dir ${MK_DIR} is exist. Skip"
  fi
}

chown_dir() {
  echo "Give permission for dir ${MK_DIR} for user ${CURRENT_USER}"
  chown -R $CURRENT_USER $MK_DIR
}

MK_DIR="/var/app/payment/postgresql/volume"
create_dir
chown_dir

MK_DIR="/var/app/payment"
chown_dir